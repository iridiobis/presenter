package es.iridiobis.presenter


import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.junit.runner.RunWith
import org.mockito.runners.MockitoJUnitRunner

import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull
import junit.framework.Assert.assertTrue
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(MockitoJUnitRunner::class)
class PresenterTest {

    @get:Rule
    val exception: ExpectedException = ExpectedException.none()

    internal var testHelper = mock(ProtectedTestHelper::class.java)
    internal var presenter = TestPresenter(testHelper)
    internal var view = mock(View::class.java)

    @Test
    fun hasView_beforeAttachment() {
        assertFalse(presenter!!.hasView())
    }

    @Test
    fun getView_beforeAttachment() {
        assertNull(presenter!!.view)
    }

    @Test
    fun attachView_alreadyAttached() {
        presenter!!.attach(view!!)
        exception.expect(IllegalStateException::class.java)
        exception.expectMessage("Already attached to a view")
        presenter!!.attach(view!!)
    }

    @Test
    fun attachView_default() {
        presenter!!.attach(view!!)
        verify<ProtectedTestHelper>(testHelper).testOnViewAttached()
        assertTrue(presenter!!.hasView())
        assertEquals(view, presenter!!.view)
    }

    @Test
    fun detachView_noAttached() {
        exception.expect(IllegalStateException::class.java)
        exception.expectMessage("Detaching a presenter not attached to a view")
        presenter!!.detach(view)
    }

    @Test
    fun detachView_attachedToOther() {
        presenter!!.attach(mock<View>(View::class.java))
        exception.expect(IllegalStateException::class.java)
        exception.expectMessage("This is not the attached view")
        presenter!!.detach(view)
    }

    @Test
    fun detachView_default() {
        presenter!!.attach(view!!)
        verify<ProtectedTestHelper>(testHelper).testOnViewAttached()
        presenter!!.detach(view)
        verify<ProtectedTestHelper>(testHelper).testBeforeViewDetached()
        assertFalse(presenter!!.hasView())
        assertNull(presenter!!.view)
    }

    interface View

    interface ProtectedTestHelper {
        fun testOnViewAttached()
        fun testBeforeViewDetached()
    }

    open class TestPresenter constructor(val testHelper: ProtectedTestHelper) : Presenter<View>() {
        override fun onViewAttached() {
            testHelper.testOnViewAttached()
        }

        override fun beforeViewDetached() {
            testHelper.testBeforeViewDetached()
        }
    }
}
