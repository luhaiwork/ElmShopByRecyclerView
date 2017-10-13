package com.example.chenlijin.elmshopbyrecyclerview;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.chenlijin.elmshopbyrecyclerview.adapter.CategoryAdapter;
import com.example.chenlijin.elmshopbyrecyclerview.adapter.TeamsAndHeaderAdapter;
import com.example.chenlijin.elmshopbyrecyclerview.bean.Category;
import com.example.chenlijin.elmshopbyrecyclerview.bean.Team;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.OnItemClickListener {

    RecyclerView recyclerviewCategory;
    RecyclerView recyclerviewTeams;

    private List<Category> categoryList;
    private CategoryAdapter categoryAdapter;
    private TeamsAndHeaderAdapter teamsAndHeaderAdapter;
    private int oldSelectedPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerviewCategory = (RecyclerView) findViewById(R.id.recyclerview_category);
        recyclerviewTeams = (RecyclerView) findViewById(R.id.recyclerview_teams);
        initData();
        initViews();
    }

    private LinearLayoutManager mTeamsLayoutManager;
    private LinearLayoutManager mCategoryLayoutManager;


    private void initViews() {
        mTeamsLayoutManager = new LinearLayoutManager(this);
        mCategoryLayoutManager = new LinearLayoutManager(this);
        recyclerviewCategory.setLayoutManager(mCategoryLayoutManager);
        recyclerviewTeams.setLayoutManager(mTeamsLayoutManager);
        categoryAdapter = new CategoryAdapter(this, categoryList);
        categoryAdapter.setOnItemClickListener(this);
        recyclerviewCategory.setAdapter(categoryAdapter);
        teamsAndHeaderAdapter = new TeamsAndHeaderAdapter(this, categoryList);
        recyclerviewTeams.setAdapter(teamsAndHeaderAdapter);

        // Add the sticky headers decoration,给球队添加标题
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(teamsAndHeaderAdapter);
        recyclerviewTeams.addItemDecoration(headersDecor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerviewTeams.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    doOnItemViewScroll();
                }
            });
        } else {
            recyclerviewTeams.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    doOnItemViewScroll();
                }
            });
        }
    }

    private void doOnItemViewScroll() {
//        if (needMove) {
//            needMove = false;
//            //获取要置顶的项在当前屏幕的位置，mIndex是记录的要置顶项在RecyclerView中的位置
//            int n = movePosition - mTeamsLayoutManager.findFirstVisibleItemPosition();
//            if (0 <= n && n < recyclerviewTeams.getChildCount()) {
//                //获取要置顶的项顶部离RecyclerView顶部的距离
//                int top = recyclerviewTeams.getChildAt(n).getTop() - dip2px(MainActivity.this, 28);
//                //最后的移动
//                recyclerviewTeams.scrollBy(0, top);
//            }
//        }
        //右侧滚动后，与左侧联动
        //第一个完全显示的item和最后一个item。
        if (!isChangeByCategoryClick) {
//            int firstVisibleItem = mTeamsLayoutManager.findFirstCompletelyVisibleItemPosition();
            int firstVisibleItem = mTeamsLayoutManager.findFirstVisibleItemPosition();
            int sort = teamsAndHeaderAdapter.getSortType(firstVisibleItem);
            changeSelected(sort);
        } else {
            isChangeByCategoryClick = false;
        }

    }

//    private boolean needMove = false;
    private int movePosition;

    private void moveToPosition(int n) {
//        //先从RecyclerView的LayoutManager中获取第一项和最后一项的Position
//        int firstItem = mTeamsLayoutManager.findFirstVisibleItemPosition();
//        int lastItem = mTeamsLayoutManager.findLastVisibleItemPosition();
//        //然后区分情况
//        if (n <= firstItem) {
//            //当要置顶的项在当前显示的第一个项的前面时
//            recyclerviewTeams.scrollToPosition(n);
//        } else if (n <= lastItem) {
//            //当要置顶的项已经在屏幕上显示时
//            int top = recyclerviewTeams.getChildAt(n - firstItem).getTop();
//            recyclerviewTeams.scrollBy(0, top - dip2px(this, 28));
//        } else {
//            //当要置顶的项在当前显示的最后一项的后面时
//            recyclerviewTeams.scrollToPosition(n);
//            movePosition = n;
//            needMove = true;
//        }
        ((LinearLayoutManager)recyclerviewTeams.getLayoutManager()).scrollToPositionWithOffset(movePosition,0);
    }

    @Override
    public void onItemClick(int position) {
        changeSelected(position);
        moveToThisSortFirstItem(position);
        isChangeByCategoryClick = true;
    }

    private void moveToThisSortFirstItem(int position) {
        movePosition = 0;
        for (int i = 0; i < position; i++) {
            movePosition += teamsAndHeaderAdapter.getCategoryList().get(i).getTeamList().size();
        }
        moveToPosition(movePosition);
    }

    private boolean isChangeByCategoryClick = false;

    private void changeSelected(int position) {
        categoryList.get(oldSelectedPosition).setSeleted(false);
        categoryList.get(position).setSeleted(true);
        //增加左侧联动
        recyclerviewCategory.scrollToPosition(position);
        oldSelectedPosition = position;
        categoryAdapter.notifyDataSetChanged();
    }

//    /**
//     * 根据手机分辨率从dp转成px
//     *
//     * @param context
//     * @param dpValue
//     * @return
//     */
//    public static int dip2px(Context context, float dpValue) {
//        final float scale = context.getResources().getDisplayMetrics().density;
//        return (int) (dpValue * scale + 0.5f);
//    }

    private void initData() {
        categoryList = new ArrayList<>();
        List<Team> teamList1 = new ArrayList<>();
        teamList1.add(new Team("多特蒙德", "http://img1.imgtn.bdimg.com/it/u=1400488354,545185599&fm=21&gp=0.jpg"));
        teamList1.add(new Team("拜仁慕尼黑", "http://img5.imgtn.bdimg.com/it/u=1016826229,3053766616&fm=21&gp=0.jpg"));
        teamList1.add(new Team("沃尔夫斯堡", "http://img2.imgtn.bdimg.com/it/u=1102871345,1624426389&fm=15&gp=0.jpg"));
        teamList1.add(new Team("门兴", "http://c.hiphotos.baidu.com/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=5d24504035fae6cd18b9a3336eda6441/eaf81a4c510fd9f91d25e41e252dd42a2834a493.jpg"));
        Category c1 = new Category("德甲", teamList1);
        c1.setSeleted(true);

        List<Team> teamList2 = new ArrayList<>();
        teamList2.add(new Team("巴塞罗那", "http://www.sinaimg.cn/lf/sports/logo85/130.png"));
        teamList2.add(new Team("皇家马德里", "http://www.sinaimg.cn/lf/sports/logo85/157.png"));
        teamList2.add(new Team("马德里竞技", "http://www.sinaimg.cn/lf/sports/logo85/162.png"));
        Category c2 = new Category("西甲", teamList2);

        List<Team> teamList3 = new ArrayList<>();
        teamList3.add(new Team("尤文图斯", "http://www.sinaimg.cn/lf/sports/logo85/108.png"));
        teamList3.add(new Team("国际米兰", "http://www.sinaimg.cn/lf/sports/logo85/103.png"));
        teamList3.add(new Team("AC米兰", "http://www.sinaimg.cn/lf/sports/logo85/104.png"));
        teamList3.add(new Team("罗马", "http://www.sinaimg.cn/lf/sports/logo85/111.png"));
        Category c3 = new Category("意甲", teamList3);

        List<Team> teamList4 = new ArrayList<>();
        teamList4.add(new Team("曼联", "http://www.sinaimg.cn/lf/sports/logo85/52.png"));
        teamList4.add(new Team("曼城", "http://www.sinaimg.cn/lf/sports/logo85/216.png"));
//        teamList4.add(new Team("切尔西","http://www.sinaimg.cn/lf/sports/logo85/60.png"));
//        teamList4.add(new Team("阿森纳","http://www.sinaimg.cn/lf/sports/logo85/61.png"));
//        teamList4.add(new Team("莱斯特成","http://www.sinaimg.cn/lf/sports/logo85/92.png"));
        Category c4 = new Category("英超", teamList4);

        List<Team> teamList5 = new ArrayList<>();
        teamList5.add(new Team("北京国安", "http://www.sinaimg.cn/ty/2015/0127/U6521P6DT20150127115830.png"));
        teamList5.add(new Team("广州恒大","http://www.sinaimg.cn/ty/2015/0127/U6521P6DT20150127124548.png"));
        teamList5.add(new Team("山东鲁能","http://www.sinaimg.cn/ty/2015/0127/U6521P6DT20150127115709.png"));
        teamList5.add(new Team("江苏苏宁","http://www.sinaimg.cn/ty/2016/0108/U6521P6DT20160108153302.png"));
        teamList5.add(new Team("上海上港","http://www.sinaimg.cn/ty/2015/0127/U6521P6DT20150127122231.png"));
        Category c5 = new Category("中超", teamList5);


        categoryList.add(c1);
        categoryList.add(c2);
        categoryList.add(c3);
        categoryList.add(c4);
        categoryList.add(c5);
//        for (int i=0;i<10;i++){
//            List<Team> teamList = new ArrayList<>();
//            teamList.add(new Team("北京国安","http://www.sinaimg.cn/ty/2015/0127/U6521P6DT20150127115830.png"));
//            teamList.add(new Team("广州恒大","http://www.sinaimg.cn/ty/2015/0127/U6521P6DT20150127124548.png"));
//            teamList.add(new Team("山东鲁能","http://www.sinaimg.cn/ty/2015/0127/U6521P6DT20150127115709.png"));
//            teamList.add(new Team("江苏苏宁","http://www.sinaimg.cn/ty/2016/0108/U6521P6DT20160108153302.png"));
//            teamList.add(new Team("上海上港","http://www.sinaimg.cn/ty/2015/0127/U6521P6DT20150127122231.png"));
//            Category c = new Category("中超",teamList);
//            categoryList.add(c);
//        }
    }
}
