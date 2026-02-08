package model;

import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

public class VideoGrabber implements AutoCloseable{
    private FFmpegFrameGrabber grabber;
    private final Java2DFrameConverter converter = new Java2DFrameConverter();
    private byte[] firstBuffer;
    private byte[] secondBuffer;
    private boolean toggle = true;

    public void openVideo(String path) throws Exception
    {
        close();
        grabber=new FFmpegFrameGrabber(path);
        grabber.start();
    }

    public int getVideoLenght()
    {
        if(grabber==null)
        {
            return 0;
        }
        return grabber.getLengthInVideoFrames();
    }

    public void setVideoToSpecificFrame(int frame) {

        try{
            grabber.setVideoFrameNumber(frame);
        }
        catch (FFmpegFrameGrabber.Exception e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] getNextFrameAsBytes() throws Exception
    {
        if(grabber == null) {return null;}

        Frame frame =grabber.grabImage();
        if(frame == null) {return null;}

        BufferedImage buffImage = converter.convert(frame);

        if(buffImage == null) {return null;}

        if (buffImage.getType() != BufferedImage.TYPE_3BYTE_BGR) {
            BufferedImage convertedImage = new BufferedImage(
                    buffImage.getWidth(),
                    buffImage.getHeight(),
                    BufferedImage.TYPE_3BYTE_BGR
            );
            convertedImage.getGraphics().drawImage(buffImage, 0, 0, null);
            buffImage = convertedImage;
        }

        byte[] result = ((DataBufferByte) buffImage.getRaster().getDataBuffer()).getData();

        if(firstBuffer == null && secondBuffer == null)
        {
            firstBuffer =new byte[result.length];
            secondBuffer = new byte[result.length];
        }

        byte[] target = toggle ? firstBuffer : secondBuffer;
        toggle=!toggle;
        System.arraycopy(result,0,target,0,result.length);
        return target;

    }

    @Override
    public void close() throws Exception
    {
        if(grabber!=null)
        {
            grabber.stop();
            grabber.release();
            grabber=null;
        }
    }

}
