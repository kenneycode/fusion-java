# fusion-java

`Android`上的`OpenGL`渲染库`java`版（[kotlin版请点击这里](https://github.com/kenneycode/fusion)）

- 高度抽象了输入输出及渲染过程，隐藏了复杂繁琐的`OpenGL API`，即使不会`OpenGL`也能轻松上手。
- 统一渲染过程，通过`RenderGraph`将渲染器按`graph`进行组织渲染。
- 支持`frame buffer`及`GL program`自动回收复用。
- 封装了`GL`线程及`EGL`环境，并自带渲染显示`View`，也可以使用系统的`GLSurfaceView`。

持续更新中...

引入方法：

根`gradle`中添加：

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

要引入的`module`中添加：

```
dependencies {
	implementation 'com.github.kenneycode:fusion-java:Tag'
}
```

基本用法：
```java
// 创建图片输入源
FusionImageSource image = new FusionImageSource(decodeBitmapFromAssets("test.png"));

// 创建一个简单渲染器
SimpleRenderer simpleRenderer = new SimpleRenderer();

// 创建RenderGraph
RenderGraph renderGraph = new RenderGraph(simpleRenderer);

// 设置RenderGraph的输出目标
renderGraph.addOutputTarget(simpleRenderer, (FusionGLTextureView) findViewById(R.id.fusionGLTextureView));

// 给输入源设置渲染器
image.addRenderer(renderGraph);

// 开始处理
image.process();
```




