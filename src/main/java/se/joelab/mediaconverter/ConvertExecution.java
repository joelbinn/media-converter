/*
 * User: joel
 * Date: 2014-01-16
 * Time: 23:06
 */
package se.joelab.mediaconverter;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.ProgressCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Executes conversion.
 */
public class ConvertExecution {
    private static ExecutorService service = Executors.newFixedThreadPool(10);
    private final String command;

    public ConvertExecution(String command) {
        this.command = command;
    }

    public Promise<Integer, Throwable, String> perform(final File src, final File dst)
            throws IOException, InterruptedException {
        // avconv/ffmpeg -i <input file> -vcodec libx264 -acodec aac -strict experimental <output file>
        final String[] commandArgs = new String[]{
                command,
                "-i", src.getAbsolutePath(),
                "-y",
                "-vcodec", "libx264",
                "-acodec", "aac",
                "-strict", "experimental",
                dst.getAbsolutePath()};

        final DeferredObject<Integer, Throwable, String> deferred = new DeferredObject<>();
        service.submit(new Runnable() {
            public void run() {
                try {
                    final Process process = Runtime.getRuntime().exec(commandArgs);
                    gobble(process.getErrorStream(), deferred);
                    gobble(process.getInputStream(), deferred);
                    deferred.resolve(process.waitFor());
                } catch (Throwable e) {
                    deferred.reject(e);
                }
            }
        });
        return deferred.promise();
    }

    private void gobble(final InputStream is, final DeferredObject<Integer, Throwable, String> deferred) {
        new Thread() {
            public void run() {
                try(BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        deferred.notify(line);
                    }
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }.start();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        new ConvertExecution("ffmpeg")
                .perform(new File("xfile.avi"), new File("xfile2.mp4"))
                .done(new DoneCallback<Integer>() {
                    @Override
                    public void onDone(Integer result) {
                        System.exit(result);
                    }
                })
                .progress(new ProgressCallback<String>() {
                    @Override
                    public void onProgress(String progress) {
                        System.out.print("\r"+progress);
                    }
                })
                .fail(new FailCallback<Throwable>() {
                    @Override
                    public void onFail(Throwable result) {
                        result.printStackTrace();
                        System.exit(1);
                    }
                });
    }
}
