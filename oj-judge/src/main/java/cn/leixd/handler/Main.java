package mo.handler;

public class Main {
    public static void main(String[] args) {
        String sensitive = "fork,exec,system,popen,<sys/ptrace.h>,<sys/wait.h>,<sys/time.h>,Runtime,Popen,subprocess,getstatusoutput(,getoutput(,pbs,apt-get";
        String src = "import os \nsystem('')";
        for (String key : sensitive.split(",")) {
            System.out.println(src.contains(key));
        }
    }
}
