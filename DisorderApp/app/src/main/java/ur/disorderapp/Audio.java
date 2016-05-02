package ur.disorderapp;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

import java.io.IOException;

public class Audio
{
    private AssetManager mAssets;
    private SoundPool mSoundPool;

    private int mID;

    private static final String TRACKS_FOLDER = "sound";


    public Audio (Context context)
    {
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);

        String[] trackNames;

        try{

            trackNames = mAssets.list(TRACKS_FOLDER);
            String path = TRACKS_FOLDER + "/" + trackNames[0];
            AssetFileDescriptor afd = mAssets.openFd(path);
            mID = mSoundPool.load(afd, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void play()
    {

        mSoundPool.play(
                mID,   // sound id
                1.0f, // left volume
                1.0f, // right volume
                1,    // priority (ignored)
                0,    // loop counter, 0 for no loop
                1.0f  // playback rate
        );
    }

    public void release()
    {
        mSoundPool.release();
    }


}
