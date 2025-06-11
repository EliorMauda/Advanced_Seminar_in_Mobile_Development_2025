package com.objectdetection.example.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.objectdetection.example.R;
import com.objectdetection.sdk.model.DetectedObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Adapter for displaying detected objects in a RecyclerView.
 */
public class DetectedObjectAdapter extends RecyclerView.Adapter<DetectedObjectAdapter.ObjectViewHolder> {

    private List<DetectedObject> objects = new ArrayList<>();
    private final Random random = new Random();

    /**
     * Updates the list of detected objects and notifies the adapter.
     *
     * @param objects The new list of objects
     */
    public void setObjects(List<DetectedObject> objects) {
        this.objects = objects != null ? objects : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ObjectViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_detected_object, parent, false);
        return new ObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ObjectViewHolder holder, int position) {
        DetectedObject object = objects.get(position);
        holder.bind(object);
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    /**
     * ViewHolder for detected object items.
     */
    class ObjectViewHolder extends RecyclerView.ViewHolder {

        private final View colorIndicator;
        private final TextView textViewLabel;
        private final TextView textViewConfidence;

        ObjectViewHolder(@NonNull View itemView) {
            super(itemView);
            colorIndicator = itemView.findViewById(R.id.colorIndicator);
            textViewLabel = itemView.findViewById(R.id.textViewLabel);
            textViewConfidence = itemView.findViewById(R.id.textViewConfidence);
        }

        void bind(DetectedObject object) {
            // Set label and confidence
            textViewLabel.setText(object.getLabel());
            textViewConfidence.setText(object.getConfidenceAsPercentage());

            // Generate a consistent color based on the label
            int color = getColorForLabel(object.getLabel());
            colorIndicator.setBackgroundColor(color);
        }

        /**
         * Generates a color based on the label text.
         * This ensures the same label always gets the same color.
         *
         * @param label The object label
         * @return A color value
         */
        private int getColorForLabel(String label) {
            // Use the label's hash code as seed for consistent colors for the same labels
            Random labelRandom = new Random(label != null ? label.hashCode() : 0);

            // Generate a bright, saturated color
            float[] hsv = new float[3];
            hsv[0] = labelRandom.nextFloat() * 360;  // Hue
            hsv[1] = 0.8f + labelRandom.nextFloat() * 0.2f;  // Saturation
            hsv[2] = 0.8f + labelRandom.nextFloat() * 0.2f;  // Value

            return Color.HSVToColor(hsv);
        }
    }
}