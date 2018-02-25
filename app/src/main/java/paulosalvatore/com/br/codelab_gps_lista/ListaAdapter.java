package paulosalvatore.com.br.codelab_gps_lista;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by paulo on 24/02/2018.
 */

public class ListaAdapter extends ArrayAdapter<Posicao> {
	private Context context;
	List<Posicao> data = null;

	public ListaAdapter(final Context context, int resource, List<Posicao> data) {
		super(context, resource, data);
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;

		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.lista_layout, parent, false);
		}

		final Posicao item = data.get(position);

		if (item != null) {
			TextView campoId = row.findViewById(R.id.campoId);
			TextView campoLatitude = row.findViewById(R.id.campoLatitude);
			TextView campoLongitude = row.findViewById(R.id.campoLongitude);
			TextView campoDataHora = row.findViewById(R.id.campoDataHora);

			campoId.setText("ID: " + item.getId());
			campoLatitude.setText("Lat: " + item.getLatitude());
			campoLongitude.setText("Lon: " + item.getLongitude());
			campoDataHora.setText("Data/Hora: " + item.getDataHora());
		}

		return row;
	}
}
