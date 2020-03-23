package build.dream.devops;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Demo {
    private static Map<String, String> map = new HashMap<String, String>();

    static {
        map.put("0", "0000");
        map.put("1", "0001");
        map.put("2", "0010");
        map.put("3", "0011");
        map.put("4", "0100");
        map.put("5", "0101");
        map.put("6", "0110");
        map.put("7", "0111");
        map.put("8", "1000");
        map.put("9", "1001");
        map.put("a", "1010");
        map.put("b", "1011");
        map.put("c", "1100");
        map.put("d", "1101");
        map.put("e", "1110");
        map.put("f", "1111");
    }

    public static String obtainData() {
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            File file = new File("/Users/liuyandong/Workspace/service-devops/note/packet.text");
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);

            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line.substring(7, 54).replaceAll(" ", ""));
            }
        } catch (Exception e) {
            if (Objects.nonNull(bufferedReader)) {
                try {
                    bufferedReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (Objects.nonNull(fileReader)) {
                try {
                    fileReader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws DecoderException {
        String hypertextTransferProtocol = "474554202f70696e672f6f6b20485454502f312e310d0a486f73743a203131312e3232392e36312e35343a393939390d0a436f6e6e656374696f6e3a206b6565702d616c6976650d0a43616368652d436f6e74726f6c3a206d61782d6167653d300d0a557067726164652d496e7365637572652d52657175657374733a20310d0a557365722d4167656e743a204d6f7a696c6c612f352e3020284d6163696e746f73683b20496e74656c204d6163204f5320582031305f31345f3129204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f38302e302e333938372e313332205361666172692f3533372e33360d0a4163636570743a20746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e382c6170706c69636174696f6e2f7369676e65642d65786368616e67653b763d62333b713d302e390d0a4163636570742d456e636f64696e673a20677a69702c206465666c6174650d0a4163636570742d4c616e67756167653a207a682d434e2c7a683b713d302e392c656e3b713d302e382c706c3b713d302e370d0a0d0a";
        System.out.println(new String(Hex.decodeHex(hypertextTransferProtocol.toCharArray())));
        // 传输层
        String transmissionControlProtocol = "fff3270fe3b3077286626e245018100030810000474554202f70696e672f6f6b20485454502f312e310d0a486f73743a203131312e3232392e36312e35343a393939390d0a436f6e6e656374696f6e3a206b6565702d616c6976650d0a43616368652d436f6e74726f6c3a206d61782d6167653d300d0a557067726164652d496e7365637572652d52657175657374733a20310d0a557365722d4167656e743a204d6f7a696c6c612f352e3020284d6163696e746f73683b20496e74656c204d6163204f5320582031305f31345f3129204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f38302e302e333938372e313332205361666172692f3533372e33360d0a4163636570743a20746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e382c6170706c69636174696f6e2f7369676e65642d65786368616e67653b763d62333b713d302e390d0a4163636570742d456e636f64696e673a20677a69702c206465666c6174650d0a4163636570742d4c616e67756167653a207a682d434e2c7a683b713d302e392c656e3b713d302e382c706c3b713d302e370d0a0d0a";

        // 网络层
        String internetProtocol = "4500020b000040004006ca27c0a801026fe53d36fff3270fe3b3077286626e245018100030810000474554202f70696e672f6f6b20485454502f312e310d0a486f73743a203131312e3232392e36312e35343a393939390d0a436f6e6e656374696f6e3a206b6565702d616c6976650d0a43616368652d436f6e74726f6c3a206d61782d6167653d300d0a557067726164652d496e7365637572652d52657175657374733a20310d0a557365722d4167656e743a204d6f7a696c6c612f352e3020284d6163696e746f73683b20496e74656c204d6163204f5320582031305f31345f3129204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f38302e302e333938372e313332205361666172692f3533372e33360d0a4163636570743a20746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e382c6170706c69636174696f6e2f7369676e65642d65786368616e67653b763d62333b713d302e390d0a4163636570742d456e636f64696e673a20677a69702c206465666c6174650d0a4163636570742d4c616e67756167653a207a682d434e2c7a683b713d302e392c656e3b713d302e382c706c3b713d302e370d0a0d0a";

        // 数据链路层
        String ethernetIi = "9c32a9b797b8784f4360756008004500020b000040004006ca27c0a801026fe53d36fff3270fe3b3077286626e245018100030810000474554202f70696e672f6f6b20485454502f312e310d0a486f73743a203131312e3232392e36312e35343a393939390d0a436f6e6e656374696f6e3a206b6565702d616c6976650d0a43616368652d436f6e74726f6c3a206d61782d6167653d300d0a557067726164652d496e7365637572652d52657175657374733a20310d0a557365722d4167656e743a204d6f7a696c6c612f352e3020284d6163696e746f73683b20496e74656c204d6163204f5320582031305f31345f3129204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f38302e302e333938372e313332205361666172692f3533372e33360d0a4163636570743a20746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e382c6170706c69636174696f6e2f7369676e65642d65786368616e67653b763d62333b713d302e390d0a4163636570742d456e636f64696e673a20677a69702c206465666c6174650d0a4163636570742d4c616e67756167653a207a682d434e2c7a683b713d302e392c656e3b713d302e382c706c3b713d302e370d0a0d0a";

        // 物理层
        String frame = "9c32a9b797b8784f4360756008004500020b000040004006ca27c0a801026fe53d36fff3270fe3b3077286626e245018100030810000474554202f70696e672f6f6b20485454502f312e310d0a486f73743a203131312e3232392e36312e35343a393939390d0a436f6e6e656374696f6e3a206b6565702d616c6976650d0a43616368652d436f6e74726f6c3a206d61782d6167653d300d0a557067726164652d496e7365637572652d52657175657374733a20310d0a557365722d4167656e743a204d6f7a696c6c612f352e3020284d6163696e746f73683b20496e74656c204d6163204f5320582031305f31345f3129204170706c655765624b69742f3533372e333620284b48544d4c2c206c696b65204765636b6f29204368726f6d652f38302e302e333938372e313332205361666172692f3533372e33360d0a4163636570743a20746578742f68746d6c2c6170706c69636174696f6e2f7868746d6c2b786d6c2c6170706c69636174696f6e2f786d6c3b713d302e392c696d6167652f776562702c696d6167652f61706e672c2a2f2a3b713d302e382c6170706c69636174696f6e2f7369676e65642d65786368616e67653b763d62333b713d302e390d0a4163636570742d456e636f64696e673a20677a69702c206465666c6174650d0a4163636570742d4c616e67756167653a207a682d434e2c7a683b713d302e392c656e3b713d302e382c706c3b713d302e370d0a0d0a";

        parseTransmissionControlProtocol(transmissionControlProtocol);
//        parseInternetProtocol(internetProtocol);
//        parseEthernetIi(ethernetIi);
    }

    public static void parseEthernetIi(String ethernetIi) {
        String source = ethernetIi.substring(0, 12);
        String destination = ethernetIi.substring(12, 24);
        String type = ethernetIi.substring(24, 28);
        System.out.println();
    }

    public static void parseInternetProtocol(String internetProtocol) {
        String binaryString = hexToBinary(internetProtocol);

        int version = Integer.parseInt(binaryString.substring(0, 4), 2);
        int headerLength = Integer.parseInt(binaryString.substring(4, 8), 2);

        String differentiatedServicesField = binaryString.substring(8, 16);
        int totalLength = Integer.parseInt(binaryString.substring(16, 32), 2);
        int identification = Integer.parseInt(binaryString.substring(32, 48), 2);
        String flags = binaryString.substring(48, 51);
        int fragmentOffset = Integer.parseInt(binaryString.substring(51, 64), 2);
        int timeToLive = Integer.parseInt(binaryString.substring(64, 72), 2);
        int protocol = Integer.parseInt(binaryString.substring(72, 80), 2);
        int headerChecksum = Integer.parseInt(binaryString.substring(80, 96), 2);
        String source = Integer.parseInt(binaryString.substring(96, 104), 2) + "." + Integer.parseInt(binaryString.substring(104, 112), 2) + "." + Integer.parseInt(binaryString.substring(112, 120), 2) + "." + Integer.parseInt(binaryString.substring(120, 128), 2);
        String destination = Integer.parseInt(binaryString.substring(128, 136), 2) + "." + Integer.parseInt(binaryString.substring(136, 144), 2) + "." + Integer.parseInt(binaryString.substring(144, 152), 2) + "." + Integer.parseInt(binaryString.substring(152, 160), 2);
        System.out.println(version);
    }

    public static TcpPacket parseTransmissionControlProtocol(String transmissionControlProtocol) throws DecoderException {
        String binaryString = hexToBinary(transmissionControlProtocol);
        int sourcePort = Integer.parseInt(binaryString.substring(0, 16), 2);
        int destinationPort = Integer.parseInt(binaryString.substring(16, 32), 2);
        long sequenceNumber = Long.parseLong(binaryString.substring(32, 64), 2);
        long acknowledgmentNumber = Long.parseLong(binaryString.substring(64, 96), 2);
        int headerLength = Integer.parseInt(binaryString.substring(96, 100), 2);
        int reserved = Integer.parseInt(binaryString.substring(100, 106), 2);
        int urg = Integer.parseInt(binaryString.substring(106, 107), 2);
        int ack = Integer.parseInt(binaryString.substring(107, 108), 2);
        int psh = Integer.parseInt(binaryString.substring(108, 109), 2);
        int rst = Integer.parseInt(binaryString.substring(109, 110), 2);
        int syn = Integer.parseInt(binaryString.substring(110, 111), 2);
        int fin = Integer.parseInt(binaryString.substring(111, 112), 2);
        int window = Integer.parseInt(binaryString.substring(112, 128), 2);
        int checksum = Integer.parseInt(binaryString.substring(128, 144), 2);
        int urgentPointer = Integer.parseInt(binaryString.substring(144, 160), 2);

        int currentKind = -1;
        int currentIndex = 160;
        while (currentKind != 0 && currentIndex != headerLength * 32) {
            int kind = Integer.parseInt(binaryString.substring(currentIndex, currentIndex + 8), 2);
            currentIndex += 8;
            currentKind = kind;

            if (kind == 0) {
                System.out.println("EOL");
                continue;
            }

            if (kind == 1) {
                System.out.println("NOP");
                continue;
            }

            if (kind == 2) {
                currentIndex += 8;
                int info = Integer.parseInt(binaryString.substring(currentIndex, currentIndex + 16), 2);
                currentIndex += 16;
                System.out.println("MSS=" + info);
            }

            if (kind == 3) {
                currentIndex += 8;
                int info = Integer.parseInt(binaryString.substring(currentIndex, currentIndex + 8), 2);
                currentIndex += 8;
                System.out.println("WSOPT=" + info);
            }

            if (kind == 4) {
                currentIndex += 8;
                System.out.println("SACK-Premitted");
            }

            if (kind == 5) {

            }

            if (kind == 8) {
                currentIndex += 8;
                long tSval = Long.parseLong(binaryString.substring(currentIndex, currentIndex + 32), 2);
                currentIndex += 32;

                long tSecr = Long.parseLong(binaryString.substring(currentIndex, currentIndex + 32), 2);
                currentIndex += 32;
                System.out.println("TSPOT, TSval=" + tSval + ", TSecr=" + tSecr);
            }
        }

        while ((currentIndex / 8) % 4 != 0) {
            currentIndex++;
        }

        if (currentIndex != binaryString.length()) {
            String data = transmissionControlProtocol.substring(currentIndex / 4);
            System.out.println("数据：\n" + new String(Hex.decodeHex(data.toCharArray())));
        }

        TcpPacket tcpPacket = new TcpPacket();
        tcpPacket.setSourcePort(sourcePort);
        tcpPacket.setDestinationPort(destinationPort);
        tcpPacket.setSequenceNumber(sequenceNumber);
        tcpPacket.setAcknowledgmentNumber(acknowledgmentNumber);
        tcpPacket.setHeaderLength(headerLength);
        tcpPacket.setReserved(reserved);
        tcpPacket.setUrg(urg);
        tcpPacket.setAck(ack);
        tcpPacket.setPsh(psh);
        tcpPacket.setRst(rst);
        tcpPacket.setSyn(syn);
        tcpPacket.setFin(fin);
        tcpPacket.setWindow(window);
        tcpPacket.setChecksum(checksum);
        tcpPacket.setUrgentPointer(urgentPointer);
        return tcpPacket;
    }

    public static String hexToBinary(String hexString) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < hexString.length(); index++) {
            stringBuilder.append(map.get(hexString.substring(index, index + 1)));
        }
        return stringBuilder.toString();
    }
}
