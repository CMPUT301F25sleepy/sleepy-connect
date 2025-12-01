package com.example.sleepy_connect;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.tasks.OnSuccessListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EventDALTest {

    @Mock
    CollectionReference mockEventsRef;

    @Mock
    DocumentReference mockCounterRef;

    @Mock
    DocumentReference mockEventDoc;

    @Mock
    Task<Void> mockSetTask;

    @Mock
    Task<DocumentSnapshot> mockGetDocTask;

    @Mock
    DocumentSnapshot mockDocumentSnapshot;

    EventDAL dal;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        dal = new EventDAL(mockEventsRef, mockCounterRef);
    }

    // test add event
    @Test
    public void testAddEvent() {
        Event e = new Event();
        e.eventID = "67";

        // making firestore
        when(mockEventsRef.document("67")).thenReturn(mockEventDoc);
        when(mockEventDoc.set(e)).thenReturn(mockSetTask);

        // make the listeners go
        doAnswer(invocation -> {
            OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return mockSetTask;
        }).when(mockSetTask).addOnSuccessListener(any(OnSuccessListener.class));

        dal.addEvent(e);

        verify(mockEventsRef).document("67");
        verify(mockEventDoc).set(e);
        verify(mockSetTask).addOnSuccessListener(any(OnSuccessListener.class));
    }

    // testing get event when it is in firebase
    @Test
    public void testGetEventGood() {
        Event expected = new Event();
        expected.eventID = "1";

        when(mockEventsRef.document("1")).thenReturn(mockEventDoc);
        when(mockEventDoc.get()).thenReturn(mockGetDocTask);

        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.toObject(Event.class)).thenReturn(expected);

        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockDocumentSnapshot);
            return mockGetDocTask;
        }).when(mockGetDocTask).addOnSuccessListener(any());

        dal.getEvent("1", event -> {
            assert event != null;
            assert event.eventID.equals("1");
        });

        verify(mockEventsRef).document("1");
        verify(mockEventDoc).get();
        verify(mockGetDocTask).addOnSuccessListener(any(OnSuccessListener.class));
    }

    // testing get event when not in firebase
    @Test
    public void testGetEventBad() {

        when(mockEventsRef.document("1")).thenReturn(mockEventDoc);
        when(mockEventDoc.get()).thenReturn(mockGetDocTask);

        when(mockDocumentSnapshot.exists()).thenReturn(false);

        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockDocumentSnapshot);
            return mockGetDocTask;
        }).when(mockGetDocTask).addOnSuccessListener(any());

        dal.getEvent("1", event -> {
            assert event == null;
        });

        verify(mockEventsRef).document("1");
        verify(mockEventDoc).get();
    }

    // test id gen
    @Test
    public void testGetNextID() {
        Task<Void> mockUpdateTask = mock(Task.class);

        // check if it returns 5
        when(mockCounterRef.get()).thenReturn(mockGetDocTask);
        when(mockDocumentSnapshot.getLong("nextID")).thenReturn(5L);

        // check if returns +1
        when(mockCounterRef.update(eq("nextID"), eq(6L))).thenReturn(mockUpdateTask);

        // get success
        doAnswer(invocation -> {
            OnSuccessListener<DocumentSnapshot> listener = invocation.getArgument(0);
            listener.onSuccess(mockDocumentSnapshot);
            return mockGetDocTask;
        }).when(mockGetDocTask).addOnSuccessListener(any());

        // update success
        doAnswer(invocation -> {
            OnSuccessListener<Void> listener = invocation.getArgument(0);
            listener.onSuccess(null);
            return mockUpdateTask;
        }).when(mockUpdateTask).addOnSuccessListener(any());

        dal.getNextID(id -> {
            assert id.equals("5");  // old id
        });

        verify(mockCounterRef).update("nextID", 6L);
    }
}