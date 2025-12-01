package com.example.sleepy_connect;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CommunityCentreDALTest {

    @Mock
    CollectionReference mockCollection;

    @Mock
    DocumentReference mockDocRef;

    @Mock
    Task<Void> mockTask;

    CommunityCentreDAL dal;

    @Before
    public void setup() {
        dal = new CommunityCentreDAL(mockCollection);
    }

    @Test
    public void testAddCommunityCentre() {
        // make comunity centre
        CommunityCentre cc = new CommunityCentre("TestCentre", "TestLocation");
        when(mockCollection.document("TestCentre")).thenReturn(mockDocRef);
        when(mockDocRef.set(cc)).thenReturn(mockTask);

        // mock the listeners for firebase
        when(mockTask.addOnSuccessListener(any())).thenReturn(mockTask);
        when(mockTask.addOnFailureListener(any())).thenReturn(mockTask);

        // add with dal and check the call
        dal.addCommunityCentre(cc);
        verify(mockCollection).document("TestCentre");
        verify(mockDocRef).set(cc);
    }
}
