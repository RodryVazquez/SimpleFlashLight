///////////////////////////////////////////////////////////////////////////////
// Exam AND-401
// Simple Flash Light
// By: Rodrigo Vazquez
//

package com.example.rodrigo.simpleflashlight;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.CharacterPickerDialog;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import java.security.Policy;

//Activty principal
public class MainActivity extends ActionBarActivity {


    private android.hardware.Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    android.hardware.Camera.Parameters parameters;

    //Al iniciar la activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Obtener la refencia al boton
        ToggleButton btnFlashButton = (ToggleButton) findViewById(R.id.btnFlash_Switch);

        //Verificamos que el dispositivo soporte el flash de la camara
        hasFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        //Si el dispositivo no soporta el flash, mostramos una alerta y cerramos la aplicacion
        if (!hasFlash) {
            AlertDialog alert = new AlertDialog.Builder(this).create();
            alert.setTitle("Error");
            alert.setMessage("Your device doesn't support flash light!!");
            alert.setButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            alert.show();
        }

        //Habilitamos el evento al cambiar de estado en el toggle
        btnFlashButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    turnOnFlash();
                } else {
                    turnOffFlash();
                }
            }
        });

    }

    //Al iniciar el ciclo de vida visible, obtenemos la camara
    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    //Al finalizar el ciclo de vida activo, apagamos el flash
    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    //Al finalizar el ciclo de vida visible, liberamos la camara
    @Override
    protected void onStop() {
        super.onStop();
        if(camera != null){
            camera.release();
            camera = null;
        }
    }

    //Inicializamos la camara
    private void getCamera() {
        if (camera == null) {
            try {
                camera = android.hardware.Camera.open();
                parameters = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Camera Error. Error: ", e.getMessage());
            }
        }
    }

    //Encendemos el flash de la camara
    private void turnOnFlash() {
        //Si el flash se encuentra apagado
        if (!isFlashOn) {
            if (camera == null || parameters == null) {
                return;
            }
            parameters = camera.getParameters();
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();
            isFlashOn = true;

            Log.v("Android ATC", "Flash has been turned on ...");
        }
    }

    //Apagamos el flash de la camaras
    private void turnOffFlash() {
        //Si el flash se encuentra encendido
        if (isFlashOn) {
            if (camera == null || parameters == null) {
                return;
            }
            parameters = camera.getParameters();
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
            camera.stopPreview();
            isFlashOn = false;

            Log.v("Android ATC", "Flash has been turned off ...");
        }
    }
}
