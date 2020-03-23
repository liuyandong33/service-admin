package build.dream.devops;

public class TcpPacket {
    /**
     * 源端口：占2字节.端口是传输层与应用层的服务接口.传输层的复用和分用功能都要通过端口才能实现
     */
    private int sourcePort;

    /**
     * 目的端口：占2字节.端口是传输层与应用层的服务接口.传输层的复用和分用功能都要通过端口才能实现
     */
    private int destinationPort;

    /**
     * 序号：占4字节.TCP 连接中传送的数据流中的每一个字节都编上一个序号.序号字段的值则指的是本报文段所发送的数据的第一个字节的序号
     */
    private long sequenceNumber;

    /**
     * 确认号：占 4 字节,是期望收到对方的下一个报文段的数据的第一个字节的序号
     */
    private long acknowledgmentNumber;

    /**
     * 数据偏移/首部长度：占 4 位,它指出 TCP 报文段的数据起始处距离 TCP 报文段的起始处有多远.“数据偏移”的单位是 32 位字(以 4 字节为计算单位)
     */
    private int headerLength;

    /**
     * 保留：占 6 位,保留为今后使用,但目前应置为 0
     */
    private int reserved;

    /**
     * 紧急URG: 当 URG=1 时,表明紧急指针字段有效.它告诉系统此报文段中有紧急数据,应尽快传送(相当于高优先级的数据)
     */
    private int urg;

    /**
     * 确认ACK：只有当 ACK=1 时确认号字段才有效.当 ACK=0 时,确认号无效
     */
    private int ack;

    /**
     * PSH(PuSH)：接收 TCP 收到 PSH = 1 的报文段,就尽快地交付接收应用进程,而不再等到整个缓存都填满了后再向上交付
     */
    private int psh;

    /**
     * RST (ReSeT):　　当 RST=1 时,表明 TCP 连接中出现严重差错（如由于主机崩溃或其他原因）,必须释放连接,然后再重新建立运输连接
     */
    private int rst;

    /**
     * 同步 SYN:　　同步 SYN = 1 表示这是一个连接请求或连接接受报文
     */
    private int syn;

    /**
     * 终止 FIN:　　用来释放一个连接.FIN=1 表明此报文段的发送端的数据已发送完毕,并要求释放运输连接
     */
    private int fin;

    /**
     *
     */
    private int window;

    /**
     * 检验和:　　占 2 字节.检验和字段检验的范围包括首部和数据这两部分.在计算检验和时,要在 TCP 报文段的前面加上 12 字节的伪首部
     */
    private int checksum;

    /**
     * 紧急指针:　　占 16 位,指出在本报文段中紧急数据共有多少个字节（紧急数据放在本报文段数据的最前面）
     */
    private int urgentPointer;

    public int getSourcePort() {
        return sourcePort;
    }

    public void setSourcePort(int sourcePort) {
        this.sourcePort = sourcePort;
    }

    public int getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(int destinationPort) {
        this.destinationPort = destinationPort;
    }

    public long getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(long sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public long getAcknowledgmentNumber() {
        return acknowledgmentNumber;
    }

    public void setAcknowledgmentNumber(long acknowledgmentNumber) {
        this.acknowledgmentNumber = acknowledgmentNumber;
    }

    public int getHeaderLength() {
        return headerLength;
    }

    public void setHeaderLength(int headerLength) {
        this.headerLength = headerLength;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public int getUrg() {
        return urg;
    }

    public void setUrg(int urg) {
        this.urg = urg;
    }

    public int getAck() {
        return ack;
    }

    public void setAck(int ack) {
        this.ack = ack;
    }

    public int getPsh() {
        return psh;
    }

    public void setPsh(int psh) {
        this.psh = psh;
    }

    public int getRst() {
        return rst;
    }

    public void setRst(int rst) {
        this.rst = rst;
    }

    public int getSyn() {
        return syn;
    }

    public void setSyn(int syn) {
        this.syn = syn;
    }

    public int getFin() {
        return fin;
    }

    public void setFin(int fin) {
        this.fin = fin;
    }

    public int getWindow() {
        return window;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public int getChecksum() {
        return checksum;
    }

    public void setChecksum(int checksum) {
        this.checksum = checksum;
    }

    public int getUrgentPointer() {
        return urgentPointer;
    }

    public void setUrgentPointer(int urgentPointer) {
        this.urgentPointer = urgentPointer;
    }
}
