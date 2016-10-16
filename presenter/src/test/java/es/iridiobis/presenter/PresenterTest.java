package es.iridiobis.presenter;


import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class PresenterTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Mock
    Presenter<View> presenter;
    @Mock
    View view;

    @Test
    public void hasView_beforeAttachment() {
        assertFalse(presenter.hasView());
    }

    @Test
    public void getView_beforeAttachment() {
        assertNull(presenter.getView());
    }

    @Test
    public void attachView_alreadyAttached() {
        presenter.attach(view);
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Already attached to a view");
        presenter.attach(view);
    }

    @Test
    public void attachView_default() {
        presenter.attach(view);
        verify(presenter).onViewAttached();
        verifyNoMoreInteractions(presenter);
        assertTrue(presenter.hasView());
        assertEquals(view, presenter.getView());
    }

    @Test
    public void detachView_noAttached() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("Detaching a presenter not attached to a view");
        presenter.detach(view);
    }

    @Test
    public void detachView_attachedToOther() {
        presenter.attach(mock(View.class));
        exception.expect(IllegalStateException.class);
        exception.expectMessage("This is not the attached view");
        presenter.detach(view);
    }

    @Test
    public void detachView_default() {
        presenter.attach(view);
        verify(presenter).onViewAttached();
        presenter.detach(view);
        verify(presenter).beforeViewDetached();
        verifyNoMoreInteractions(presenter);
        assertFalse(presenter.hasView());
        assertNull(presenter.getView());
    }

    interface View {
    }
}
