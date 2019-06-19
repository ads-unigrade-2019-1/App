import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleTest{
    private Solo solo;

    @Rule
    public MyActivityTestRule<MainActivity> activityTestRule = new MyActivityTestRule<>(MainActivity.class, true, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(activityTestRule.getInstrumentation(), activityTestRule.getActivity());
    }

    @Test
    public void testBtnExample(){
        //indo para a pagina de add matérias
        solo.clickOnButton(solo.getString(R.id.subjectsFragment));


        //solo.clickOnButton(solo.getString(R.id.));
    }

}
