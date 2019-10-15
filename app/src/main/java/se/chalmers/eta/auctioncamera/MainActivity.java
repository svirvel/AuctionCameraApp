package se.chalmers.eta.auctioncamera;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.AndroidViewModel;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_PHOTO = 1;

    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void addNewAuctionItem(View view)
    {
        EditText idEditText = findViewById(R.id.id_editText);
        EditText yearEditText = findViewById(R.id.year_editText);

        String photoName = yearEditText.getText().toString() + String.format("%04d", Integer.parseInt(idEditText.getText().toString()));

        photoFile = null;

        try {
            photoFile = createImageFile(photoName);
        } catch (IOException ex) {
            Toast myToast = Toast.makeText(this, "Error creating file.",
                    Toast.LENGTH_SHORT);
            myToast.show();
            return;
        }

        if(photoFile.exists()) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

            alertDialogBuilder.setMessage("File exists! Do you want to continue?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    takePhoto(photoFile);
                }
            });

            alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    return;
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            return;
        }

        takePhoto(photoFile);

    }

    private void takePhoto(File file) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            Uri photoURI = FileProvider.getUriForFile(this,"se.chalmers.eta.auctioncamera.fileProvider", file);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }


    private File createImageFile(String name) throws IOException {
        // Create an image file name

        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = new File(storageDir.getAbsolutePath() +"/" + name + ".jpg");

        // Save a file: path for use with ACTION_VIEW intents
        return image;
    }
}
