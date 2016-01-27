package team.birdhead.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.birdhead.licenseview.R;

public class LicenseView extends FrameLayout {

    static final String DEFAULT_PATH = "license";

    String mPath;
    ListView mListView;
    LicenseAdapter mAdapter;

    public LicenseView(Context context) {
        super(context);

        initialize(context, null, 0, 0);
    }

    public LicenseView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initialize(context, attrs, 0, 0);
    }

    public LicenseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initialize(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LicenseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        initialize(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initialize(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LicenseView, defStyleAttr, defStyleRes);
        try {
            mAdapter = createAdapter(context, a.getResourceId(R.styleable.LicenseView_item_layout, R.layout.view_license_item));
            mListView = createListView(context, a.getResourceId(R.styleable.LicenseView_list_layout, R.layout.view_license));
            mListView.setAdapter(mAdapter);

            setPath(a.hasValue(R.styleable.LicenseView_path) ? a.getString(R.styleable.LicenseView_path) : DEFAULT_PATH);
        } finally {
            a.recycle();
        }
    }

    protected ListView createListView(Context context, int layoutResourceId) {
        LayoutInflater.from(context).inflate(layoutResourceId, this, true);
        return (ListView) findViewById(R.id.list);
    }

    protected LicenseAdapter createAdapter(Context context, int itemLayoutResourceId) {
        return new LicenseAdapter(context, itemLayoutResourceId);
    }

    public void setPath(String path) {
        if (!TextUtils.equals(mPath, path)) {
            mPath = path;
            mAdapter.setPath(path);
            mAdapter.notifyDataSetChanged();
        }
    }

    protected static class LicenseAdapter extends BaseAdapter {

        protected final Context mContext;
        protected final LayoutInflater mInflater;
        protected final int mLayoutResourceId;
        protected final List<String> mFileNames;
        protected final Map<String, Item> mItems;
        protected String mPath;

        public LicenseAdapter(Context context, int layoutResourceId) {
            mContext = context;
            mInflater = LayoutInflater.from(context);
            mLayoutResourceId = layoutResourceId;
            mFileNames = new ArrayList<>();
            mItems = new HashMap<>();
        }

        public void setPath(String path) {
            mPath = path;
            mFileNames.clear();
            mItems.clear();

            try {
                Collections.addAll(mFileNames, mContext.getAssets().list(path));
            } catch (IOException e) {
                throw new IllegalArgumentException("read license directory error:" + path, e);
            }
        }

        @Override
        public int getCount() {
            return mFileNames.size();
        }

        @Override
        public Item getItem(int position) {
            final String fileName = mFileNames.get(position);
            final Item item;
            if (mItems.containsKey(fileName)) {
                item = mItems.get(fileName);
            } else {
                item = createItem(fileName);
                mItems.put(fileName, item);
            }
            return item;
        }

        protected Item createItem(String fileName) {
            return new Item(fileName, readLicense(mPath + "/" + fileName));
        }

        protected String readLicense(String fileName) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(mContext.getAssets().open(fileName)));

                String line;
                final StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (IOException e) {
                throw new IllegalStateException("read license file error:" + fileName, e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return false;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getConvertView(position, convertView, parent);
            final ViewHolder holder = getViewHolder(convertView);
            holder.setItem(getItem(position));
            return convertView;
        }

        protected View getConvertView(int position, View convertView, ViewGroup parent) {
            return convertView != null ? convertView : mInflater.inflate(mLayoutResourceId, parent, false);
        }

        protected ViewHolder getViewHolder(View convertView) {
            return convertView.getTag() == null ? ViewHolder.newInstance(convertView) : ViewHolder.getInstance(convertView);
        }
    }

    protected static class ViewHolder {

        protected final TextView mTitleView;
        protected final TextView mLicenseView;

        public static ViewHolder newInstance(View convertView) {
            return new ViewHolder(convertView);
        }

        public static ViewHolder getInstance(View convertView) {
            return (ViewHolder) convertView.getTag();
        }

        protected ViewHolder(View convertView) {
            mTitleView = (TextView) convertView.findViewById(R.id.title);
            mLicenseView = (TextView) convertView.findViewById(R.id.message);

            convertView.setTag(this);
        }

        public void setItem(Item item) {
            mTitleView.setText(item.mTitle);
            mLicenseView.setText(item.mLicense);
        }
    }

    protected static class Item {

        public final String mTitle;
        public final String mLicense;

        public Item(String title, String license) {
            mTitle = title;
            mLicense = license;
        }
    }
}
