package service.parser;

class ParserThread extends Thread {
    private String str;

    public void setStr(String str) {
        this.str = str;
    }

    @Override
    public void run() {
        Parser parser = new Parser(str);
        parser.run();
    }
}
