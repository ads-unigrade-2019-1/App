import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;
import android.view.View;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.robotium.solo.Solo;
import com.unigrade.app.R;
import com.unigrade.app.View.Activity.MainActivity;

import org.junit.Before;
import org.junit.BeforeClass;
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

    @BeforeClass
    public static void init() throws Exception{
        InstrumentationRegistry.getTargetContext().deleteDatabase("Unigrade");
    }

    @Test
    public void insertCourse(){
        solo.sleep(2000);
        View viewCampus = solo.getView(R.id.campus_spinner, 0);
        solo.clickOnView(viewCampus);
        solo.sleep(100);
        solo.clickOnView(solo.getView(TextView.class, 3)); 
        solo.sleep(150);
        View viewCourse = solo.getView(R.id.course_spinner, 0);
        solo.clickOnView(viewCourse);
        solo.sleep(100);
        solo.clickOnView(solo.getView(TextView.class, 3)); 
        solo.sleep(150);
        solo.clickOnView(solo.getView(R.id.btn_continue));
        solo.sleep(300);
    }

    @Test
    public void addUserClass(){
        //vai para a pagina de add matérias
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.subjectsFragment));
        solo.sleep(500);

        //adiciona texto na pesquisa
        solo.enterText(0, "algoritmo");
        solo.sleep(500);
        //clica na primeira matéria encontrada
        solo.clickInList(0);
        solo.sleep(200);
        //salva o nome da matéria
        CharSequence subjectName = ((TextView)solo.getView(R.id.class_title)).getText();
        //seleciona a primeira e a segunda turma da matéria
        if(!solo.isCheckBoxChecked(0)){
            solo.clickOnCheckBox(0);
            solo.sleep(500);
        }
        if(!solo.isCheckBoxChecked(1)){
            solo.clickOnCheckBox(1);
            solo.sleep(500);
        }

        solo.goBack();
        //vai para a tela de turmas adicionadas
        solo.clickOnView(solo.getView(R.id.userSubjectsFragment));
        solo.sleep(1000);
        //Clica na última matéria adicionada
        solo.clickInList(1);
        //salva o nome da matéria que foi adicionada
        CharSequence addedSubjectName = ((TextView)solo.getView(R.id.class_title)).getText();
        //Verifica se o nome da matéria adicionada é o esperado
        assertEquals(subjectName, addedSubjectName);
        //Verifica se a primeira turma está selecionada
        assertTrue(solo.isCheckBoxChecked(0));
        //Verifica se a segunda turma est selecionada
        assertTrue(solo.isCheckBoxChecked(1));

        solo.goBack();
        assertTrue(solo.searchText((String)subjectName));
    }

    @Test
    public void generateTimetables(){
        //vai para a pagina de add matérias
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.subjectsFragment));
        solo.sleep(500);

        //adiciona texto na pesquisa
        solo.enterText(0, "algoritmo");
        solo.sleep(500);
        //clica na primeira matéria encontrada
        solo.clickInList(0);
        solo.sleep(200);
        //salva o nome da matéria
        CharSequence subjectName = ((TextView)solo.getView(R.id.class_title)).getText();
        //seleciona a primeira e a segunda turma da matéria
        if(!solo.isCheckBoxChecked(0)){
            solo.clickOnCheckBox(0);
            solo.sleep(500);
        }
        if(!solo.isCheckBoxChecked(1)){
            solo.clickOnCheckBox(1);
            solo.sleep(500);
        }

        solo.goBack();
        solo.clickOnView(solo.getView(R.id.timetablesFragment));
        solo.clickOnView(solo.getView(R.id.btn_visualize));
        solo.sleep(300);

        assertTrue(solo.getView(R.id.timetables_list).isShown());
    }

    @Test
    public void flowShow(){
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.flowFragment));
        solo.sleep(300);

        assertTrue(solo.getView(R.id.period_list).isShown());
    }

    @Test
    public void removeUserClass(){
        //vai para a pagina de add matérias
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.subjectsFragment));
        solo.sleep(500);

        //adiciona texto na pesquisa
        solo.enterText(0, "testes");
        solo.sleep(500);
        //clica na primeira matéria encontrada
        solo.clickInList(0);
        solo.sleep(200);

        //seleciona a primeira e a segunda turma da matéria
        if(!solo.isCheckBoxChecked(0)){
            solo.clickOnCheckBox(0);
            solo.sleep(500);
        }

        solo.goBack();
        //vai para a tela de turmas adicionadas
        solo.clickOnView(solo.getView(R.id.userSubjectsFragment));
        solo.sleep(1000);
        //Clica na última matéria adicionada
        solo.clickInList(1);
        //salva o nome da matéria que foi adicionada
        String subjectName = (String)((TextView)solo.getView(R.id.class_title)).getText();
        //desmarca turmas anteriormente marcadas
        if(solo.isCheckBoxChecked(0)){
            solo.clickOnCheckBox(0);
        }
        solo.goBack();
        //Verifica se a turma foi removida da lista de turmas do usuário
        assertFalse(solo.searchText(subjectName));
    }
}