package com.moore.sortdemo;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    private List<City> cities;
    private Map<String, List<City>> datas;
    private List<List<City>> cityData;
    private IndexView mIndexView;
    private ExpandableListView mExpandableListView;
    private ExpandAdapter mAdapter;

    private String[] citiesStr = new String[]{"123", "1234", "**", "北京", "崇州", "大连", "峨眉山", "北海", "眉山", "屏峰", "盐城", "日照", "乔司", "贺州", "桂林", "百色", "荆门", "襄阳", "安康", "开封", "金华", "南通", "台北", "韶关", "重庆", "临沂", "宿迁", "贵阳", "三明", "淮安", "洛阳", "徐州", "驻马店", "郑州", "南京", "上海", "赣州", "广州", "杭州", "嘉兴", "绍兴", "宁波", "台州", "温州", "兰州", "厦门", "福州", "龙岩", "合肥", "蚌埠", "南昌", "吉安", "上饶", "乌鲁木齐"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Locale.setDefault(Locale.CHINA);
        initData();

        mIndexView = (IndexView) findViewById(R.id.indexView);
        mExpandableListView = (ExpandableListView) findViewById(R.id.expand_listView);

        cityData = new ArrayList<>();
        datas.forEach((key, value) -> {
            //分组里的子项排序
            Collections.sort(value);
            cityData.add(value);
        });
        //父项排序
        Collections.sort(cityData, (o1, o2) -> o1.get(0).getFirstLetter().compareTo(o2.get(0).getFirstLetter()));

        mAdapter = new ExpandAdapter(this, cityData);
        mExpandableListView.setAdapter(mAdapter);
        //设置箭头不显示
        mExpandableListView.setGroupIndicator(null);
        //默认展开所有
        for (int i = 0; i < mAdapter.getGroupCount(); i++) {
            mExpandableListView.expandGroup(i);
        }
        //设置父节点不可点击
        mExpandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> true);
        //快速定位栏监听
        mIndexView.setOnIndexChangedListener((index, indexLetter) -> {
            for (int i = 0; i < cityData.size(); i++) {
                if (indexLetter.equals(cityData.get(i).get(0).getFirstLetter())) {
                    int position = 0;
                    for (int j = 0; j < i; j++) {
                        position += cityData.get(j).size();
                    }
                    position += i;
                    mExpandableListView.smoothScrollToPositionFromTop(position, 0, 0);
                    break;
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void initData() {
        cities = new ArrayList<>();
        for (int i = 0; i < citiesStr.length; i++) {
            City city = new City(citiesStr[i]);
            cities.add(city);
        }
        cities.forEach(city -> {
            String cityName = city.getCityName();
            if (cityName == null || "".equals(cityName))
                return;
            //如果不是中文，则归类到#分类
            Pattern pattern = Pattern.compile("([\\u4e00-\\u9fa5]{1})");
            if (!pattern.matcher(cityName.substring(0, 1)).find()) {
                city.setFirstLetter("#");
                city.setCityPinYin(city.getCityName());
            } else {
                ArrayList<HanziToPinyin.Token> tokens = HanziToPinyin.getInstance().get(city.getCityName());
                if (tokens.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    tokens.forEach(token -> {
                        sb.append(token.target);
                    });
                    String firstLetter = tokens.get(0).target.substring(0, 1);
                    city.setFirstLetter(firstLetter);
                    city.setCityPinYin(sb.toString());
                }
            }
        });
        //根据首字母分类
        datas = cities.stream().collect(Collectors.groupingBy(City::getFirstLetter));
    }
}
