package com.yichuang.ml_demo.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.qualcomm.qti.snpe.FloatTensor;
import com.qualcomm.qti.snpe.NeuralNetwork;
import com.qualcomm.qti.snpe.SNPE;
import com.yichuang.ml_demo.R;
import com.yichuang.ml_demo.tool.Tools;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class NumberRecognitionActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TF_MOBILE_MODEL_FILENAME = "mnist_model.pb";
    private static final String SNPE_MODEL_FILENAME = "mnist.dlc";
    private static final String IMAGE_FILENAME = "t10k-images.idx3-ubyte";
    private static final String LABEL_FILENAME = "t10k-labels.idx1-ubyte";

    private static final String INPUT_DATA_NAME = "x:0";
    private static final String OUTPUT_SCORES_NAME = "y:0";

    private static final int MESSAGE_LOAD_MODEL = 100;
    private static final int MESSAGE_LOAD_DATA = 101;
    private static final int MESSAGE_RECOGNIZE = 102;

    private static final int MODEL_TENSORFLOW_MOBILE = 0;
    private static final int MODEL_SNPE = 1;

    private static final String TAG = "NumberRecognitionActivity";

    private TensorFlowInferenceInterface inferenceInterface;
    private NeuralNetwork SNPENeuralNetwork;

    private HandlerThread workerThread;
    private Handler workerHandler;

    private ListView listView;
    private Button startButton;
    private TextView timeCostTv;
    private RadioGroup nnFrameworks;

    private MyAdapter myAdapter;

    private ImageItem[] items;
    private boolean recognized = false;
    private int selectedModel = MODEL_TENSORFLOW_MOBILE;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numer_recognize);
        initView();

        // 加载模型.
        inferenceInterface = new TensorFlowInferenceInterface(getAssets(), TF_MOBILE_MODEL_FILENAME);
        workerThread = new HandlerThread("worker-thread");
        workerThread.start();
        workerHandler = new WorkerHandler(workerThread.getLooper());
        workerHandler.sendEmptyMessage(MESSAGE_LOAD_MODEL);
        workerHandler.sendEmptyMessage(MESSAGE_LOAD_DATA);
    }

    private void initView() {
        listView = findViewById(R.id.listview);
        startButton = findViewById(R.id.bt_start);
        startButton.setOnClickListener(this);
        timeCostTv = findViewById(R.id.time_cost);
        nnFrameworks = findViewById(R.id.framework_groups);
        nnFrameworks.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_tf_mobile:
                        selectedModel = MODEL_TENSORFLOW_MOBILE;
                        break;
                    case R.id.rb_snpe:
                        selectedModel = MODEL_SNPE;
                        break;
                }
            }
        });
        nnFrameworks.check(R.id.rb_tf_mobile);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_start:
                startButton.setEnabled(false);
                workerHandler.sendEmptyMessage(MESSAGE_RECOGNIZE);
                break;
        }
    }

    private final class WorkerHandler extends Handler {
        public WorkerHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_LOAD_MODEL:
                    loadSNPEModel();
                case MESSAGE_LOAD_DATA:
                    loadData();
                    break;
                case MESSAGE_RECOGNIZE:
                    recognize();
            }
        }
    }

    private void loadSNPEModel() {
        NeuralNetwork network = null;
        try {
            InputStream is = getAssets().open(SNPE_MODEL_FILENAME);
            final SNPE.NeuralNetworkBuilder builder = new SNPE.NeuralNetworkBuilder(getApplication())
                    .setDebugEnabled(false)
                    .setRuntimeOrder(NeuralNetwork.Runtime.CPU)
                    .setModel(is, 32294);
            network = builder.build();
        } catch (IllegalStateException | IOException e) {
            throw new RuntimeException(e);
        }
        final NeuralNetwork result = network;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SNPENeuralNetwork = result;
            }
        });
    }

    private void recognize() {
        long begin = System.currentTimeMillis();
        int rightCount = 0;
        if (selectedModel == MODEL_TENSORFLOW_MOBILE) {
            String[] outputScoresNames = new String[]{OUTPUT_SCORES_NAME};
            float[] outputScores = new float[10];

            for (int i = 0; i < items.length; i++) {
                // Run the model.
                float[] temp = new float[items[i].pixels.length];
                for (int j = 0; j < items[i].pixels.length; j++) {
                    temp[j] = items[i].pixels[j];
                }
                inferenceInterface.feed(INPUT_DATA_NAME, temp, 1, 28 * 28);
                inferenceInterface.run(outputScoresNames);
                inferenceInterface.fetch(OUTPUT_SCORES_NAME, outputScores);
                items[i].result = Tools.findMaxIndexInArray(outputScores);
                if (items[i].result == items[i].label) {
                    rightCount++;
                }
//            Log.d(TAG, "recognize result: " + items[i].result);
//            for (int j = 0; j < outputScores.length; j++) {
//                Log.d(TAG, j + ": " + outputScores[j]);
//            }
//            Log.d(TAG, "----------------------------");
            }
        } else if (selectedModel == MODEL_SNPE) {
            final FloatTensor inputTensor = SNPENeuralNetwork.createFloatTensor(
                    SNPENeuralNetwork.getInputTensorsShapes().get(INPUT_DATA_NAME));
            final int[] dimensions = inputTensor.getShape();
            final Map<String, FloatTensor> inputs = new HashMap<>();
            for (int i = 0; i < items.length; i++) {
                float[] temp = new float[items[i].pixels.length];
                for (int j = 0; j < items[i].pixels.length; j++) {
                    temp[j] = items[i].pixels[j];
                }
                inputTensor.write(temp, 0, 28 * 28);
                inputs.put(INPUT_DATA_NAME, inputTensor);
                Map<String, FloatTensor> outputs = SNPENeuralNetwork.execute(inputs);
                for (Map.Entry<String, FloatTensor> output : outputs.entrySet()) {
                    if (output.getKey().equals(OUTPUT_SCORES_NAME)) {
                        FloatTensor outputTensor = output.getValue();
                        final float[] array = new float[outputTensor.getSize()];
                        outputTensor.read(array, 0, array.length);
//                        for (int j = 0; j < array.length; j++) {
//                            Log.d(TAG, j + ": " + array[j]);
//                        }
                        items[i].result = Tools.findMaxIndexInArray(array);
                        if (items[i].result == items[i].label) {
                            rightCount++;
                        }
                        outputTensor.release();
                    }
                }
            }
            if (inputTensor != null) {
                inputTensor.release();
            }
        }
        final long time_cost = (System.currentTimeMillis() - begin);
        final float rightRate = rightCount * 1.0f / items.length;
        recognized = true;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startButton.setEnabled(true);
                myAdapter.notifyDataSetChanged();
                timeCostTv.setText("花费时间：" + time_cost + "ms,正确率：" + (rightRate));
            }
        });
    }

    private void loadData() {
        InputStream is = null;
        try {
            loadImages(is);
            loadLabels(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        showItems();
    }

    private void loadImages(InputStream is) throws IOException {
        is = getAssets().open(IMAGE_FILENAME);
        byte[] buffer = new byte[4];

        is.read(buffer);
        Log.d(TAG, "magic number:" + Tools.byteArrayToInt(buffer));

        is.read(buffer);
        int itemNumber = Tools.byteArrayToInt(buffer);
        Log.d(TAG, "number of images:" + itemNumber);
        items = new ImageItem[itemNumber];

        is.read(buffer);
        int rows = Tools.byteArrayToInt(buffer);
        Log.d(TAG, "rows:" + rows);

        is.read(buffer);
        int columns = Tools.byteArrayToInt(buffer);
        Log.d(TAG, "columns:" + columns);

        byte[] pixels = new byte[rows * columns];
        int n = 0;
        while (is.read(pixels) != -1) {
            int[] colors = new int[pixels.length];
            for (int i = 0; i < pixels.length; i++) {
                colors[i] = Tools.byteToInt(pixels[i]);
            }
            ImageItem item = new ImageItem();
            item.pixels = colors;
            items[n] = item;
            n++;
        }
        Log.d(TAG, "actual number of items:" + n);
    }

    private void loadLabels(InputStream is) throws IOException {
        is = getAssets().open(LABEL_FILENAME);
        byte[] buffer = new byte[4];
        is.read(buffer);
        Log.d(TAG, "magic number:" + Tools.byteArrayToInt(buffer));
        is.read(buffer);
        int itemNumber = Tools.byteArrayToInt(buffer);
        Log.d(TAG, "number of items:" + itemNumber);
        byte[] b = new byte[1];
        int n = 0;
        while (is.read(b) != -1 && n <= itemNumber) {
            items[n].label = b[0];
            n++;
        }
        Log.d(TAG, "actual number of items:" + n);
    }

    private class ImageItem {
        int label;
        int result = -1;
        int[] pixels;
    }

    private void showItems() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                myAdapter = new MyAdapter(items, NumberRecognitionActivity.this);
                listView.setAdapter(myAdapter);
            }
        });
    }

    private class MyAdapter extends BaseAdapter {

        private ImageItem[] imageItems;
        private Context context;

        public MyAdapter(ImageItem[] imageItems, Context context) {
            this.imageItems = imageItems;
            this.context = context;
        }

        @Override
        public int getCount() {
            return imageItems.length;
        }

        @Override
        public Object getItem(int position) {
            return imageItems[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.listview_item,
                        parent, false);
                holder = new ViewHolder();
                holder.index = convertView.findViewById(R.id.index);
                holder.image = convertView.findViewById(R.id.image);
                holder.label = convertView.findViewById(R.id.label);
                holder.result = convertView.findViewById(R.id.result);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.index.setText(position + "");
            ImageItem item = imageItems[position];
            Bitmap bitmap = Bitmap.createBitmap(item.pixels, 28, 28, Bitmap.Config.RGB_565);
            holder.image.setImageBitmap(bitmap);
            holder.label.setText(item.label + "");
            holder.result.setText(item.result + "");
            if ((item.result != item.label) && recognized) {
                holder.result.setTextColor(Color.RED);
            } else {
                holder.result.setTextColor(Color.GRAY);
            }

            return convertView;
        }
    }

    private class ViewHolder {
        TextView index;
        ImageView image;
        TextView label;
        TextView result;
    }
}
