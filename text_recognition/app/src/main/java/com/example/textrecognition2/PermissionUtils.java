package com.example.textrecognition2;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * <h1>esta actividad se encarga de comprobar que los permisos han sido proporcionados</h1>
 */
public class PermissionUtils {

    /**
     * Solicita los permisos oportunos a la aplicacion
     * @param activity Actividad que solicita los permisos
     * @param requestCode Codigo de peticion
     * @param permissions Permisos necesarios
     * @return True si se han garantizado los permisos, False en otro caso
     */
    public static boolean requestPermission(Activity activity, int requestCode, String... permissions) {
        boolean granted = true;
        ArrayList<String> permissionsNeeded = new ArrayList<>();

        for (String s : permissions) {
            int permissionCheck = ContextCompat.checkSelfPermission(activity, s);
            boolean hasPermission = (permissionCheck == PackageManager.PERMISSION_GRANTED);
            granted &= hasPermission;
            if (!hasPermission) {
                permissionsNeeded.add(s);
            }
        }

        if (granted) {
            return true;
        } else {
            ActivityCompat.requestPermissions(activity,
                    permissionsNeeded.toArray(new String[permissionsNeeded.size()]),
                    requestCode);
            return false;
        }
    }

    /**
     * Informa si la app tiene los permisos adecuados
     * @param requestCode Codigo de validacion para comprobar si se tienen los permisos o no
     * @param permissionCode Codigo que indica el permiso solicitado
     * @param grantResults Resultados de las peticiones de los permidos
     * @return True si se tienen permisos, False en otro caso
     */
    public static boolean permissionGranted(int requestCode, int permissionCode, int[] grantResults) {
        return requestCode == permissionCode && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
