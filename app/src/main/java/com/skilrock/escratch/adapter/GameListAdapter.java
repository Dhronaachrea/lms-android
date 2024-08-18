package com.skilrock.escratch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skilrock.config.Config;
import com.skilrock.customui.AmountTextView;
import com.skilrock.escratch.bean.GameListDataBean;
import com.skilrock.escratch.bean.IGEUnfinishGameData;
import com.skilrock.escratch.util.ImageLoader;
import com.skilrock.lms.ui.R;
import com.skilrock.utils.AmountFormat;
import com.skilrock.utils.GlobalVariables;
import com.skilrock.utils.LotteryPreferences;
import com.squareup.picasso.Picasso;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by abhishekd on 5/23/2015.
 */
public class GameListAdapter extends ArrayAdapter<Object> {

    private Context mContext;
    private ArrayList<GameListDataBean.Games> gameDatas;
    private ArrayList<IGEUnfinishGameData.UnfinishedGameList> gameLists;
    private int layoutId;
    private int width;
    private ImageLoader imageLoader;

    public GameListAdapter(Context context, int resource, ArrayList<GameListDataBean.Games> gameses) {
        super(context, resource, (ArrayList) gameses);
        this.mContext = context;
        this.layoutId = resource;
        this.gameDatas = gameses;
        imageLoader = new ImageLoader(mContext);
    }

    public GameListAdapter(Context context, int resource, List<IGEUnfinishGameData.UnfinishedGameList> gameses) {
        super(context, resource, (List) gameses);
        this.mContext = context;
        this.layoutId = resource;
        this.gameLists = (ArrayList<IGEUnfinishGameData.UnfinishedGameList>) gameses;
        imageLoader = new ImageLoader(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Holder holder = null;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new Holder();
            width = (new LotteryPreferences(getContext()).getWIDTH()) - GlobalVariables.getPx((int) getContext().getResources().getDimension(R.dimen._34sdp), getContext());
            // view = inflater.inflate(layoutId, parent, false);
//            holder.header = (TextView) view.findViewById(R.id.title);
//            holder.description = (TextView) view.findViewById(R.id.artist);
//            holder.price = (TextView) view.findViewById(R.id.duration);
//            holder.image = (ImageView) view.findViewById(R.id.list_image);


            view = inflater.inflate(layoutId, parent, false);

            holder.imageView = (ImageView) view.findViewById(R.id.image);
            holder.gameName = (TextView) view.findViewById(R.id.gameName);
            holder.gamePrice = (AmountTextView) view.findViewById(R.id.gamePrice);
            view.setTag(holder);
        }
        holder = (Holder) view.getTag();

        if (gameDatas != null) {
            GameListDataBean.Games games = gameDatas.get(position);
            holder.gameName.setText(games.getGameName());
            holder.gamePrice.setCurrencySymbol(Config.CURRENCY_SYMBOL);
            holder.gamePrice.setText(AmountFormat.getAmountFormatForSingleDecimal(games.getGamePrice()) + "");
//        holder.header.setText(games.getGameName());
//        holder.description.setText(games.getGameDescription());
//        holder.price.setText("$"+games.getGamePrice()+" ");
            if (Config.isStatic && GlobalVariables.loadDummyData) {
                new FetchImageFromURLTask(holder.imageView).execute(games.getGameImageLocations().getImagePath());

            } else {
                Picasso.with(mContext).load(games.getGameImageLocations().getImagePath()).resize(width / 3, width / 3).placeholder(R.drawable.placeholder).into(holder.imageView);
            }
//            Picasso.with(mContext).load(games.getGameImageLocations().getImagePath()).placeholder(R.drawable.placeholder).into(holder.imageView);
        } else {
            IGEUnfinishGameData.UnfinishedGameList gameList = gameLists.get(position);
            holder.gameName.setText(gameList.getGameMaster().getGameName());
            holder.gamePrice.setVisibility(View.VISIBLE);
            holder.gamePrice.setCurrencySymbol(Config.CURRENCY_SYMBOL);
            holder.gamePrice.setText(AmountFormat.getAmountFormatForSingleDecimal(gameList.getGameMaster().getGamePrice()));
            if (Config.isStatic && GlobalVariables.loadDummyData) {
                new FetchImageFromURLTask(holder.imageView).execute(gameList.getGameMaster().getGameImageName());
            } else {
                Picasso.with(mContext).load(gameList.getGameMaster().getGameImageName()).resize(width / 3, width / 3).placeholder(R.drawable.placeholder).into(holder.imageView);
            }
        }
        // We should use class holder pattern
//        TextView tv = (TextView) result.findViewById(R.id.txt1);
//        tv.setText(itemList.get(position));

        return view;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    private class Holder {
        ImageView imageView;
        TextView gameName;
        AmountTextView gamePrice;
    }

//    private class Holder {
//        ImageView image;
//        TextView header, description, price;
//    }

    private class FetchImageFromURLTask extends AsyncTask<String, Void, Bitmap> {
        private WeakReference vRef;
        private String imgDesc = "";

        public FetchImageFromURLTask(ImageView v) {
            vRef = new WeakReference(v);
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            //  this.imgDesc = params[1];
            String path = params[0];
            return imageLoader.getImageInBitmap(path);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            final ImageView iv = (ImageView) vRef.get();
            if (bitmap != null)
                iv.setImageBitmap(Bitmap.createScaledBitmap(bitmap, width / 3, width / 3, false));
        }
    }
}
