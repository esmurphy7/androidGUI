package ca.titanoboa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Created by QA Test Account on 7/23/2014.
 */
public class ConfigPromptActivity extends Activity {

    public static final String IS_SIMULATION = "ca.titanoboa.IS_SIMULATION";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.configprompt);
        Button proceedButton = (Button) findViewById(R.id.proceedButton);
        proceedButton.setOnClickListener(new proceedButtonListener());

    }

    /**
     * Listener for proceed button on initial config prompt layout.
     * Selecting the simulation config will initialize the simulation object,
     * acting as a flag for the simulation environment
     */
    private class proceedButtonListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            RadioGroup configGroup = (RadioGroup) findViewById(R.id.radioConfig);
            int configSelectedId = configGroup.getCheckedRadioButtonId();
            RadioButton selectedConfig = (RadioButton) findViewById(configSelectedId);
            RadioButton titanoboaConfig = (RadioButton) findViewById(R.id.titanoboa);
            RadioButton simulationConfig = (RadioButton) findViewById(R.id.simulation);

            Intent intent = new Intent(ConfigPromptActivity.this, TitanoboaControlActivity.class);
            if(selectedConfig == simulationConfig){
                intent.putExtra(IS_SIMULATION, true);
            }else{
                intent.putExtra(IS_SIMULATION, false);
            }
            startActivity(intent);
        }
    }
}