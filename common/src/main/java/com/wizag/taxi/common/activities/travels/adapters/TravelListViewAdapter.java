package com.wizag.taxi.common.activities.travels.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.wizag.taxi.common.R;
import com.wizag.taxi.common.databinding.ItemTravelBinding;
import com.wizag.taxi.common.models.Travel;
import com.wizag.taxi.common.utils.DateConverter;
import com.ramotion.foldingcell.FoldingCell;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class TravelListViewAdapter extends ArrayAdapter<Travel> {

    private HashSet<Integer> unfoldedIndexes = new HashSet<>();
    private View.OnClickListener hideTravelClickListener;
    private View.OnClickListener writeComplaintClickListener;

    public TravelListViewAdapter(Context context, List<Travel> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // get item for selected view
        Travel travel = getItem(position);
        // if cell is exists - reuse it, if not - create the new one from resource
        FoldingCell cell = (FoldingCell) convertView;
        ViewHolder viewHolder;
        if (cell == null) {
            viewHolder = new ViewHolder();
            LayoutInflater vi = LayoutInflater.from(getContext());
            viewHolder.binding = DataBindingUtil.inflate(vi, R.layout.item_travel, parent, false);
            cell = (FoldingCell) viewHolder.binding.getRoot();
            cell.setTag(viewHolder);
        } else {
            // for existing cell set valid valid state(without animation)
            if (unfoldedIndexes.contains(position)) {
                cell.unfold(true);
            } else {
                cell.fold(true);
            }
            viewHolder = (ViewHolder) cell.getTag();
        }

        viewHolder.binding.setItem(travel);
        if (travel == null)
            return cell;
        viewHolder.binding.textFrom.setSelected(true);
        viewHolder.binding.textTo.setSelected(true);
        viewHolder.binding.titleFromAddress.setSelected(true);
        viewHolder.binding.titleToAddress.setSelected(true);

        if (travel.getRequestTime() != null) {
            Date dateRequest = new Date();
            dateRequest.setTime(travel.getRequestTime().getTime());
            if (getContext().getResources().getBoolean(R.bool.use_date_converter)) {
                viewHolder.binding.titleDateLabel.setText(DateConverter.getDate(dateRequest));
                viewHolder.binding.textRequestDate.setText(DateConverter.getDate(dateRequest));
            } else {
                viewHolder.binding.titleDateLabel.setText(new SimpleDateFormat("EEE, MMM d", Locale.getDefault()).format(dateRequest));
                viewHolder.binding.textRequestDate.setText(new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(dateRequest));
            }
            viewHolder.binding.titleTimeLabel.setText(new SimpleDateFormat("hh:mm aaa", Locale.getDefault()).format(dateRequest));
            viewHolder.binding.textRequestTime.setText(new SimpleDateFormat("hh:mm aaa", Locale.getDefault()).format(dateRequest));
        }
        if (travel.getFinishTimestamp() != null) {
            Date dateFinish = new Date();
            dateFinish.setTime(travel.getFinishTimestamp().getTime());
            if (getContext().getResources().getBoolean(R.bool.use_date_converter)) {
                viewHolder.binding.textFinishDate.setText(DateConverter.getDate(dateFinish));
            } else {
                viewHolder.binding.textFinishDate.setText(new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(dateFinish));
            }
            viewHolder.binding.textFinishTime.setText(new SimpleDateFormat("hh:mm aaa", Locale.getDefault()).format(dateFinish));
        }
        viewHolder.binding.textCost.setText(getContext().getString(R.string.unit_money, travel.getCost()));
        viewHolder.binding.textDetailsCost.setText(getContext().getString(R.string.unit_money, travel.getCost()));
        int sec = travel.getDurationReal() % 60;
        int min = travel.getDurationReal() / 60;
        viewHolder.binding.textDuration.setText(String.format(Locale.getDefault(), "%02d:%02d", min, sec));
        viewHolder.binding.textDetailsDistance.setText(getContext().getString(R.string.unit_distance, (travel.getDistanceReal() / 1000f)));
        String mapUrl = "https://maps.googleapis.com/maps/api/staticmap?size=600x400&language=" + getContext().getString(R.string.default_language) + "&markers=color:blue|" + travel.getPickupPoint().latitude + "," + travel.getPickupPoint().longitude + "&markers=color:green|" + travel.getDestinationPoint().latitude + "," + travel.getDestinationPoint().longitude;
        if (travel.getLog() != null && !travel.getLog().isEmpty())
            mapUrl += "&path=weight:3|color:orange|enc:" + travel.getLog();
        travel.setImageUrl(mapUrl);
        viewHolder.binding.buttonComplaint.setTag(travel.getId());
        viewHolder.binding.buttonHideTravel.setTag(travel.getId());
        viewHolder.binding.buttonComplaint.setOnClickListener(getWriteComplaintClickListener());
        viewHolder.binding.buttonHideTravel.setOnClickListener(getHideTravelClickListener());
        Resources res = getContext().getResources();
        if (travel.getStatus() != null) {
            String status = res.getString(res.getIdentifier("travel_status_" + travel.getStatus().replace(" ", "_").toLowerCase(), "string", getContext().getPackageName()));
            viewHolder.binding.textStatus.setText(status);
        }
        return cell;
    }

    // simple methods for register cell state changes
    public void registerToggle(int position) {
        if (unfoldedIndexes.contains(position))
            registerFold(position);
        else
            registerUnfold(position);
    }

    public void registerFold(int position) {
        unfoldedIndexes.remove(position);
    }

    public void registerUnfold(int position) {
        unfoldedIndexes.add(position);
        Travel travel = getItem(position);
        notifyDataSetChanged();
    }

    public View.OnClickListener getHideTravelClickListener() {
        return hideTravelClickListener;
    }

    public void setHideTravelClickListener(View.OnClickListener hideTravelClickListener) {
        this.hideTravelClickListener = hideTravelClickListener;
    }

    public View.OnClickListener getWriteComplaintClickListener() {
        return writeComplaintClickListener;
    }

    public void setWriteComplaintClickListener(View.OnClickListener writeComplaintClickListener) {
        this.writeComplaintClickListener = writeComplaintClickListener;
    }

    // View lookup cache
    private static class ViewHolder {
        ItemTravelBinding binding;
    }
}
