package com.example.controlepragas;

import java.io.IOException;
import java.io.InputStream;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.jjoe64.graphview.BarGraphView;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphViewDataInterface;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.GraphViewSeries.GraphViewSeriesStyle;
import com.jjoe64.graphview.ValueDependentColor;

public class FragGraph extends Fragment {

	public static String AssetJSONFile (String filename, Context context) throws IOException {
        AssetManager manager = context.getAssets();
        InputStream file = manager.open(filename);
        byte[] formArray = new byte[file.available()];
        file.read(formArray);
        file.close();

        return new String(formArray);
    }
	
	public static FragGraph newInstance() {
		FragGraph fragment = new FragGraph();
		return fragment;
	}

	public FragGraph() {
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final View viewRoot = inflater.inflate(R.layout.fragment_graph, null);
		
		AQuery aq = new AQuery(getActivity());
		aq.ajax("http://10.92.101.35/MonitoramentoRoedores/consultatudo.php", JSONArray.class, 
				new AjaxCallback<JSONArray>(){
			@Override
			public void callback(String url, JSONArray object, AjaxStatus status) {
				Log.e("controlepragas", ""+status.getMessage());
				int qtd = object.length();
				Log.e("controlepragas", ""+qtd);
				
				// TODO Auto-generated method stub
				GraphViewSeriesStyle seriesStyle = new GraphViewSeriesStyle();
				seriesStyle.setValueDependentColor(new ValueDependentColor() {
					
					@Override
				  public int get(GraphViewDataInterface data) {
				    // the higher the more red
				    return Color.rgb((int)(150+((data.getY()/3)*100)), (int)(150-((data.getY()/3)*150)), (int)(150-((data.getY()/3)*150)));
				  }
				});
				
				GraphViewData[] data = null;
				try {
		            String rootStr = AssetJSONFile("graph.json", getActivity());
		            JSONObject root = new JSONObject(rootStr);
		            JSONArray array = root.getJSONArray("kits");
		            
		            data = new GraphViewData[array.length()];
		            for (int i = 0; i < array.length(); i++){
		            	JSONObject kit = array.getJSONObject(i);
		            	data[i] = new GraphViewData(kit.getInt("name"), i==0?qtd:kit.getInt("qtd"));
		            }
		        } catch (IOException e) {
		            e.printStackTrace();
		        } catch (JSONException e) {
		            e.printStackTrace();
		        }
				
				GraphViewSeries exampleSeries2 = new GraphViewSeries("aaa", seriesStyle, data);
				
				GraphView view = new BarGraphView(getActivity(), "Equipamentos");
				view.addSeries(exampleSeries2);
				
				((FrameLayout)viewRoot.findViewById(R.id.contGraph)).addView(view);
			}
		});
		
		
		
		return viewRoot;
	}
	
}
