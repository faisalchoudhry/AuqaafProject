package ubaidgul.appdeveloper.auqaf.upload_later;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import ubaidgul.appdeveloper.auqaf.R;
import ubaidgul.appdeveloper.auqaf.helper.Helper;


public class ItemAdapter extends ArrayAdapter<String> {

    private Context context;
    public static ArrayList<String> Ids;
    private int rowResourceId;
    private Upload up;
    public static int pos = -1;

    final int THUMBNAIL_SIZE = 50;
    String imageFile = "";
    Bitmap b = null;

    Helper helper;
    String tableName;

    String url;
    String clickButton;


    public ItemAdapter(Context context, int textViewResourceId, ArrayList<String> objects, String tableName, String url, String clickedButton) {
        super(context, textViewResourceId, objects);
        this.context = context;
        Ids = objects;
        this.rowResourceId = textViewResourceId;
        up = new Upload();
        this.tableName = tableName;
        this.helper = new Helper(context);
        this.url = url;
        this.clickButton = clickedButton;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater;
        View rowView = null;
        if (rowView==null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(rowResourceId, parent, false);
        }
        try {
            final ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView);
            final Button btnUploads = (Button) rowView.findViewById(R.id.btnUpload);
            TextView tvCircle = (TextView) rowView.findViewById(R.id.tvCircle);
            TextView tvSrNum = (TextView) rowView.findViewById(R.id.tvSrNum);
            TextView tvPUNum = (TextView) rowView.findViewById(R.id.tvPUNum);
            TextView tvLocality = (TextView) rowView.findViewById(R.id.tvLocality);
            TextView tvWard = (TextView) rowView.findViewById(R.id.tvWard);
            TextView tvBlock = (TextView) rowView.findViewById(R.id.tvBlock);
            TextView tvDateTime = (TextView) rowView.findViewById(R.id.tvDateTime);

            final int id = Integer.parseInt(Ids.get(position));
            JSONObject jsonObject = new JSONObject(UploaderGui.array1[id]);

            String plantType ;

//            if(jsonObject.getString("receiver_comp_name").equalsIgnoreCase("Other type")){
//                tvCircle.setText("Plant Type: " + jsonObject.getString("other_plant_type"));
//            }else{
//                tvCircle.setText("Plant Type: " + jsonObject.getString("plant_type"));
//            }


            tvLocality.setText("Scheme ID: " + jsonObject.getString("scheme_id"));
            tvSrNum.setText("Division: " + jsonObject.getString("division"));
            tvPUNum.setText("District: " +jsonObject.getString("district"));
//            tvWard.setText("District: " + jsonObject.getString("district"));

            tvBlock.setVisibility(View.GONE);

            String datetime = UploaderGui.DateTimeIDS[id];
            String parts = datetime.substring(0, 16);
            tvDateTime.setText("Date time: " + parts);

            btnUploads.setTag(id);
            btnUploads.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        pos = Integer.parseInt(btnUploads.getTag().toString());
                        up.upload(getContext(), btnUploads.getTag().toString(), tableName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            String _path = Environment.getExternalStorageDirectory() + "/" + context.getString(R.string.images_folder) + "/";
            imageFile = _path + UploaderGui.path[id];
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 5;
            if (!UploaderGui.path[id].equalsIgnoreCase(""))
				b = BitmapFactory.decodeFile(imageFile, options);
//            b = BitmapFactory.decodeFile(imageFile);
            if (b != null) {
                Bitmap b1 = Bitmap.createScaledBitmap(b, THUMBNAIL_SIZE, THUMBNAIL_SIZE, false);
                imageView.setImageBitmap(b1);
                imageView.setTag(UploaderGui.path[id]);
            }
            imageView.setTag(id);
            imageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(imageFile)), "image/*");
                    context.startActivity(intent);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rowView;
    }
}
