package com.e.rojinapiapplication;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import model.Employee;
import model.EmployeeCUD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateDeleteEmployee extends AppCompatActivity {
    private final static String BASE_URL = "http://dummy.restapiexample.com/api/v1/";
    private Button btnSearch, btnUpdate, btnDelete;
    private EditText etEmpNo, etEmpName, etEmpSalary, etEmpAge;
    Retrofit retrofit;
    EmployeeAPI employeeAPI;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete_employee);

        btnSearch = findViewById(R.id.btnSearch);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);
        etEmpNo = findViewById(R.id.etEmpNo);
        etEmpName = findViewById(R.id.etEmpName);
        etEmpSalary = findViewById(R.id.etEmpSalary);
        etEmpAge = findViewById(R.id.etEmpAge);
        builder=new AlertDialog.Builder(this);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.setMessage("Do you want to delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                //finish();
                                deleteEmployee();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                Toast.makeText(getApplicationContext(), "You clicked No", Toast.LENGTH_SHORT).show();
                            }
                        });

                AlertDialog alert =builder.create();
                alert.setTitle("Delete")
                ;
                alert.show();


            }
        });

    }

    private void CreateInstance() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        employeeAPI = retrofit.create(EmployeeAPI.class);
    }

    private void loadData() {
        CreateInstance();
        Call<Employee> listCall = employeeAPI.getEmployeeByID(Integer.parseInt(etEmpNo.getText().toString()));

        listCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {
                etEmpName.setText(response.body().getEmployee_name());
                etEmpSalary.setText(Float.toString(response.body().getEmployee_salary()));
                etEmpAge.setText(Integer.toString(response.body().getEmployee_age()));
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(UpdateDeleteEmployee.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void updateEmployee() {
        CreateInstance();

        EmployeeCUD employee = new EmployeeCUD(
                etEmpName.getText().toString(),
                Float.parseFloat(etEmpSalary.getText().toString()),
                Integer.parseInt(etEmpAge.getText().toString())
        );

        Call<Void> voidCall = employeeAPI.updateEmployee(Integer.parseInt(etEmpNo.getText().toString()), employee);

        voidCall.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateDeleteEmployee.this, "Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateDeleteEmployee.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void deleteEmployee() {
        CreateInstance();
        Call<Void> voidCall = employeeAPI.deleteEmployee(Integer.parseInt(etEmpNo.getText().toString()));

        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateDeleteEmployee.this, "Successfully deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateDeleteEmployee.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
