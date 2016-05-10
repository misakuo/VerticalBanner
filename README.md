# VerticalBanner
一个简单粗暴的垂直滚动Banner组件，基于RecyclerView封装而来

## 在Android Studio中使用
- 在`build.gradle`中添加
```
compile 'com.moxun:vbanner:1.0.3'
```    
- 在布局文件中引入
```
<com.moxun.VerticalBannerView/>
```    
- 像设置RecyclerView一样设置Banner的属性（注意：不需要为Banner设置LayoutManager）    
- 继承`VBAdapter<D>`，实现和RecyclerView的Adapter一致的方法       
- 调用`start()`方法激活Banner 

## 定制属性 
- `setSwitchMode(int mode)`    
设置Item移出屏幕的方式，可选值`VerticalBannerView.MODE_SCROLL_OUT`向上滑出屏幕；`VerticalBannerView.MODE_FADE_OUT`在当前屏幕移除item，其余Item滚动到顶端    
- `setVisibleItemCount`     
设置Banner中可见Item的数量    
- `setAutoScrollDelay`    
设置Item切换的间隔，单位毫秒     

## NOTE    
必须在页面绘制完毕后才能调用`start()`方法，在Fragment中，可以通过`rootView.post()`方法调用`start()`，在Activity中，可通过`DecorView.post()`或ViewTreeObserver调用

## 已知问题
众所周知，在support-23.2版本之后的RecyclerView已经可以支持wrap_content属性，在使用VerticalBanner时，如果Banner的直接parent为一个可滚动的控件，那么当设置宽度为wrap_content时，Banner会测量自身的高度为所有Item高度的和，这个测量的结果是优先于onMeasure中所设置的值的，所以如果需要将Banner嵌套在滚动控件中，需要明确指定Banner的高度为一个定值或match_parent（不管设置的值是多少，最后Banner的高度都由setVisibleItemCount方法的返回值计算得来）

## 等有空了再来添加截图......
