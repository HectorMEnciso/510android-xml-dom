package net.sgoliver.android.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	private Button btnCargar,btnInsertar;
	private TextView txtResultado;
    DBController controller = new DBController(this);
	private List<Noticia> noticias;
    HashMap<String, String> queryValues =  new  HashMap<String, String>();
    ArrayList<HashMap<String, String>> noticiasList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


        btnInsertar = (Button)findViewById(R.id.InsertarBD);
		btnCargar = (Button)findViewById(R.id.btnCargar);
		txtResultado = (TextView)findViewById(R.id.txtResultado);
        noticiasList=controller.getAllNoticias();



		btnCargar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				CargarXmlTask tarea = new CargarXmlTask();
		        tarea.execute("http://212.170.237.10/rss/rss.aspx");
			}
		});

        btnInsertar.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                for(int i=0;i<noticiasList.size();i++){
                    {
                        //String titulo= noticiasList.get(i).get("title");
                       // String fecha= noticiasList.get(i).get("pubDate");

                       Noticia n = noticiasList.get(i);

                        if (!controller.existeNoticia(n.getTitulo(), n.getGuid())){
                            controller.insertNoticia(noticias.get(i));
                        }
                    }
                }
            }
        });
	}
    public void onResume(){
        super.onResume();
        for(int t=0;t<noticiasList.size();t++){
            Log.e("errorrrrrrrrrrr", noticiasList.get(t).get("title"));
            txtResultado.append(noticiasList.get(t).get("title") + "\n" +
                    noticiasList.get(t).get("link") + "\n" +
                    noticiasList.get(t).get("description") + "\n" + noticiasList.get(t).get("guid") +
                    noticiasList.get(t).get("pubDate"));
        }
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	//Tarea As�ncrona para cargar un XML en segundo plano
	private class CargarXmlTask extends AsyncTask<String,Integer,Boolean> {
		 
	    protected Boolean doInBackground(String... params) {
	 
	    	RssParserDom saxparser = new RssParserDom(params[0]);
	        
			noticias = saxparser.parse();//parsear y guarda datos en lista de noticias
	 
	        return true;
	    }
	    
	    protected void onPostExecute(Boolean result) {
	    	
	    	//Tratamos la lista de noticias
			//Por ejemplo: escribimos los t�tulos en pantalla
			txtResultado.setText("");
	        for(int i=0; i<noticias.size(); i++)
	        {
	        	txtResultado.setText(
	        		txtResultado.getText().toString() +
	        			System.getProperty("line.separator") + 
	        			noticias.get(i).getTitulo());
	        }
	    }
	}
}
