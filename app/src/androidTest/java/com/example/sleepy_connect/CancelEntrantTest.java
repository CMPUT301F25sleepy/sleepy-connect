package com.example.sleepy_connect;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.ArrayList;

public class CancelEntrantTest {

    // testing when there is an entrant
    @Test
    public void testCancelledEntrant() {
        Entrant entrant = mock(Entrant.class);
        Event event = mock(Event.class);
        EventDAL mockDal = mock(EventDAL.class);

        ArrayList<String> pending = new ArrayList<>();
        pending.add("1");

        when(entrant.getAndroid_id()).thenReturn("1");
        when(event.getPendingList()).thenReturn(pending);

        CancelEntrant ce = new CancelEntrant(mockDal);

        ce.removeCancelledEntrant(entrant, event);

        assertFalse(pending.contains("1"));
        verify(mockDal).updateEvent(event);
    }
}