package customer.app.driveaze.driveaze;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class ChooseServiceActivity extends AppCompatActivity {

    private Button btnSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_service);

        btnSubmit = findViewById(R.id.btnSubmitRequest);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ChooseServiceActivity.this, "Invoice generated", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
