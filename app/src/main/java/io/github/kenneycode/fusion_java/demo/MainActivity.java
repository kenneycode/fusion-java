package io.github.kenneycode.fusion_java.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import java.io.IOException;

import io.github.kenneycode.fusionjava.inputsource.FusionImageSource;
import io.github.kenneycode.fusionjava.outputtarget.FusionGLTextureView;
import io.github.kenneycode.fusionjava.process.RenderGraph;
import io.github.kenneycode.fusionjava.renderer.SimpleRenderer;

/**
 *
 * Coded by kenney
 *
 * http://www.github.com/kenneycode/fusion-java
 *
 * fusion demo
 *
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

    }

    private Bitmap decodeBitmapFromAssets(String filename) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        try {
            return BitmapFactory.decodeStream(getAssets().open(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
