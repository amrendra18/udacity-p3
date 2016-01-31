package barqsoft.footballscores.utils.svg;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.File;
import java.io.InputStream;

import barqsoft.footballscores.logger.Debug;

/**
 * Created by Amrendra Kumar on 31/01/16.
 * <p>
 * <p>
 * SVG Image loading code taken from
 * Glide sample app here
 * https://github.com/bumptech/glide/tree/v3.6.0/samples/svg/src/main/java/com/bumptech/svgsample/app
 */
public class SvgImageLoader {

    static SvgImageLoader mInstance = null;
    static GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> genericRequestBuilder =
            null;

    private SvgImageLoader() {

    }

    public static SvgImageLoader getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SvgImageLoader();
            genericRequestBuilder = Glide.with(context)
                    .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                    .from(Uri.class)
                    .as(SVG.class)
                    .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                    .sourceEncoder(new StreamEncoder())
                    .cacheDecoder(new FileToStreamDecoder<>(new SvgDecoder()))
                    .decoder(new SvgDecoder())
                    .listener(new SvgSoftwareLayerSetter<>());
        }
        return mInstance;
    }

    public void loadSvg(ImageView imageView, String url, int placeHolder, int error) {
        genericRequestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .load(Uri.parse(url))
                .placeholder(placeHolder)
                .error(error)
                .into(imageView);
    }

    public static void clearCache(Context context) {
        Debug.e("Clearing glide cache", false);
        Glide.get(context).clearMemory();
        File cacheDir = Glide.getPhotoCacheDir(context);
        if (cacheDir.isDirectory()) {
            for (File child : cacheDir.listFiles()) {
                if (!child.delete()) {
                    Debug.e("cannot delete: " + child, false);
                }
            }
        }
    }

}
