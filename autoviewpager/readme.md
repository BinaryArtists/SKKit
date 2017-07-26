# 轮播控件

>  基本要素：ViewPager + PagerAdapter
* AutoSlidPager 可以自动轮播的ViewPager
* ViewPagerAdapter  基础的PagerAdapter
* LoopPagerAdapter extends ViewPagerAdapter  扩展为可以无限循坏的PagerAdapter
* IconLoopPagerAdapter extends LoopPagerAdapter 扩展为可以联动指示器的PagerAdapter
* IconPageIndicator extends HorizontalScrollView 就是一个普通的横向滑动View


#### 怎样实现无限循环 ？
    - 设置 PagerAdapter 的getCount() 返回Integer.MAX_VALUE, 设置初始位置为中间的某个数值，让View 滑不完，其实是伪循坏 。


#### 怎样自动轮播 ？
    - 开启一个倒计时系统，定时调用 setCurrentItem ();  通过Handler 的 handleMessage(...)  和  sendEmptyMessageDelayed(...) 实现。


#### 怎样实现与指示器的联动？
    - 把viewPager 作为IconPagerIndicator 的一个引用，在IconPagerIndicator中设置setOnPageChangeListener(...) 来监听viewPager的滑动，
    从而来更新指示器的状态  。





