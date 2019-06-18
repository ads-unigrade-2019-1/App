import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;
import com.unigrade.app.View.Activity.MainActivity;

import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ExampleTest extends ActivityTestRule<MainActivity> {
    public ExampleTest(Class<MainActivity> activityClass) {
        super(activityClass);
    }
}
