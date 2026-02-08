package model;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HexFormat;
import java.util.List;

public class ResultExport {
    private static void writeFrameToWriters(PlasmaService.ProcessedFrame frame,/* BufferedWriter hexWriter,*/ BufferedWriter binWriter) throws IOException {
        //byte[] result = frame.hash();
        byte[] result = frame.generatedResult();

       // hexWriter.write(HexFormat.of().withUpperCase().formatHex(result));
        //hexWriter.newLine();

        StringBuilder binBuilder = new StringBuilder();
        for (byte b : result) {
            binBuilder.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
        binWriter.write(binBuilder.toString());
       // binWriter.newLine();
    }

    public static void exportResultsToFiles(List<PlasmaService.ProcessedFrame> results, String... fileNameParts) throws IOException {
        try (/*BufferedWriter hexWriter = new BufferedWriter(new FileWriter(Arrays.toString(fileNameParts) + "_HEX.txt", true));*/
             BufferedWriter binWriter = new BufferedWriter(new FileWriter(Arrays.toString(fileNameParts) + "_BIN.txt", true))) {

            for (PlasmaService.ProcessedFrame frame : results) {
                writeFrameToWriters(frame/*, hexWriter*/, binWriter);
            }
        }
    }

    public static void exportResultToFiles(PlasmaService.ProcessedFrame frame, String... fileNameParts) throws IOException {
        try (/*BufferedWriter hexWriter = new BufferedWriter(new FileWriter(Arrays.toString(fileNameParts) + "_HEX.txt", true));*/
             BufferedWriter binWriter = new BufferedWriter(new FileWriter(Arrays.toString(fileNameParts) + "_BIN.txt", true))) {

            writeFrameToWriters(frame/*, hexWriter*/, binWriter);
        }
    }
}
