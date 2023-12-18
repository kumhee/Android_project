package com.example.ex06;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.*;

public class ShopFragment extends Fragment {
    String query="애플";
    ArrayList<HashMap<String, Object>> array=new ArrayList<>();
    ShopAdapter adapter = new ShopAdapter();
    int page = 1;
    int total = 0;
    int start = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_shop, container, false);
        new ShopThread().execute();

        RecyclerView list=view.findViewById(R.id.list);
        list.setAdapter(adapter);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        list.setLayoutManager(manager);

        view.findViewById(R.id.search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page = 1;
                array = new ArrayList<>();
                EditText edtQuery = view.findViewById(R.id.query);
                query = edtQuery.getText().toString();
                new ShopThread().execute();
            }
        });

        view.findViewById(R.id.more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page >= 10) {
                    Toast.makeText(getActivity(), "마지막페이지입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    page += 1;
                    new ShopThread().execute();
                }
            }
        });
        return view;
    }

    class ShopThread extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String url="https://openapi.naver.com/v1/search/shop.json";
            start = (page-1) * 10 + 1;
            String result=NaverAPI.con(url, query, start);
            return result;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            shopParser(s);
            //System.out.println("데이터갯수:" + array.size());
            adapter.notifyDataSetChanged();
        }
    }
    public void shopParser(String result){
        try{
            //JSONObject objTotal = new JSONObject(result).getJSONObject("total");
            //total = Integer.parseInt(objTotal.toString());
            JSONArray jArray = new JSONObject(result).getJSONArray("items");
            for(int i=0; i<jArray.length(); i++){
                JSONObject obj = jArray.getJSONObject(i);
                HashMap<String, Object> map=new HashMap<>();
                map.put("title", obj.getString("title"));
                map.put("link", obj.getString("link"));
                map.put("price", obj.getInt("lprice"));
                map.put("image", obj.getString(("image")));
                array.add(map);
            }
        }catch (Exception e){
            System.out.println("파싱오류:" + e.toString());
        }
    }

    //쇼핑 어댑터
    class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.ViewHolder>{
        @NonNull
        @Override
        public ShopAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view=getLayoutInflater().inflate(R.layout.item_shop, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ShopAdapter.ViewHolder holder, int position) {
            HashMap<String, Object> map = array.get(position);
            holder.title.setText(Html.fromHtml(map.get("title").toString()));

            int intprice = Integer.parseInt(map.get("price").toString());
            DecimalFormat df = new DecimalFormat(("#,###원"));
            holder.price.setText(df.format(intprice));

            String strImage = map.get("image").toString();
            if(strImage.equals("")) {
                holder.image.setImageResource(R.drawable.shop);
            } else {
                Picasso.with(getActivity()).load(strImage).into(holder.image);
            }
        }

        @Override
        public int getItemCount() {
            return array.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView title, price;
            ImageView image;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.title);
                price = itemView.findViewById(R.id.price);
                image = itemView.findViewById(R.id.image);
            }
        }
    }
}