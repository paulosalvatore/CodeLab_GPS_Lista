package paulosalvatore.com.br.codelab_gps_lista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ListaActivity extends AppCompatActivity {

	private ListView listView;
	private DatabaseManager baseDados;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista);

		listView = findViewById(R.id.listView);

		DatabaseHelper helper = new DatabaseHelper(this.getApplicationContext());
		DatabaseManager.initializeInstance(helper);
		baseDados = DatabaseManager.getInstance();

		atualizarListaPosicoes();
	}

	private void atualizarListaPosicoes() {
		List<Posicao> listaPosicoes = baseDados.obterPosicoesUsuario();

		listView.setAdapter(
				new ListaAdapter(
						this,
						R.layout.lista_layout,
						listaPosicoes
				)
		);
	}
}
