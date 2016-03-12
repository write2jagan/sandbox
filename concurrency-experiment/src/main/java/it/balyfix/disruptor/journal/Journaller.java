package it.balyfix.disruptor.journal;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import com.lmax.disruptor.EventHandler;

import it.balyfix.disruptor.LongEvent;

public class Journaller implements EventHandler<LongEvent>
{
    private final File directory;
    private FileChannel file = null;
    private final ByteBuffer[] buffers = new ByteBuffer[2];
	public static Charset charset = Charset.forName("UTF-8");
    public static CharsetEncoder encoder = charset.newEncoder();

    public Journaller(File directory)
    {
        this.directory = directory;
        buffers[0] = ByteBuffer.allocate(4);
    }
    
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception
    {
        int size = event.toString().getBytes().length;
        
        if (null == file)
        {
            file = new RandomAccessFile(new File(directory, "jnl"), "rw").getChannel();
        }
        
        buffers[0].clear();
        buffers[0].putInt(size).flip();
        buffers[1] = encoder.encode(CharBuffer.wrap(event.toString()));
        buffers[1].clear().limit(size);
        
        while (buffers[1].hasRemaining())
        {
            file.write(buffers);
        }

        buffers[1].clear();
        buffers[1] = null;
        
        if (endOfBatch)
        {
            file.force(true);
        }
    }
}
