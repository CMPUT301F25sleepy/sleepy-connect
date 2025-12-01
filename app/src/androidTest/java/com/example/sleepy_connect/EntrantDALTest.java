package com.example.sleepy_connect;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EntrantDALTest {

    @Mock
    CollectionReference mockCollection;

    @Mock
    DocumentReference mockDocRef;

    @Mock
    Task<Void> mockVoidTask;

    @Mock
    Task<DocumentSnapshot> mockDocSnapshotTask;

    @Mock
    DocumentSnapshot mockSnapshot;

    EntrantDAL dal;

    @Before
    public void setup() {
        dal = new EntrantDAL(mockCollection);

        // add listeners for firebase
        when(mockVoidTask.addOnSuccessListener(any())).thenReturn(mockVoidTask);
        when(mockVoidTask.addOnFailureListener(any())).thenReturn(mockVoidTask);

        when(mockDocSnapshotTask.addOnSuccessListener(any())).thenReturn(mockDocSnapshotTask);
        when(mockDocSnapshotTask.addOnFailureListener(any())).thenReturn(mockDocSnapshotTask);
    }

    // test adding entrant
    @Test
    public void testAddEntrant() {
        Entrant e = new Entrant("entrant1");

        when(mockCollection.document("entrant1")).thenReturn(mockDocRef);
        when(mockDocRef.set(e)).thenReturn(mockVoidTask);

        dal.addEntrant(e);

        verify(mockCollection).document("entrant1");
        verify(mockDocRef).set(e);
    }

    // testing remove entrant
    @Test
    public void testRemoveEntrant() {
        Entrant e = new Entrant("entrant2");

        when(mockCollection.document("entrant2")).thenReturn(mockDocRef);
        when(mockDocRef.delete()).thenReturn(mockVoidTask);

        dal.removeEntrant(e);

        verify(mockCollection).document("entrant2");
        verify(mockDocRef).delete();
    }

    // testing update entrant
    @Test
    public void testUpdateEntrant() {
        Entrant e = new Entrant("entrant3");

        when(mockCollection.document("entrant3")).thenReturn(mockDocRef);
        when(mockDocRef.set(e)).thenReturn(mockVoidTask);

        dal.updateEntrant(e);

        verify(mockCollection).document("entrant3");
        verify(mockDocRef).set(e);
    }

    // check when entrant exists
    @Test
    public void testGetEntrantGood() {
        String uid = "entrant4";
        Entrant mockEntrant = new Entrant(uid);

        when(mockCollection.document(uid)).thenReturn(mockDocRef);
        when(mockDocRef.get()).thenReturn(mockDocSnapshotTask);

        when(mockSnapshot.exists()).thenReturn(true);
        when(mockSnapshot.toObject(Entrant.class)).thenReturn(mockEntrant);

        dal.getEntrant(uid, entrant -> {
            assert entrant != null;
            assert entrant.getAndroid_id().equals(uid);
        });
    }

    // check when entrant not found
    @Test
    public void testGetEntrantBad() {
        String uid = "entrant5";

        when(mockCollection.document(uid)).thenReturn(mockDocRef);
        when(mockDocRef.get()).thenReturn(mockDocSnapshotTask);

        when(mockSnapshot.exists()).thenReturn(false);

        dal.getEntrant(uid, entrant -> {
            assert entrant == null;
        });
    }
}