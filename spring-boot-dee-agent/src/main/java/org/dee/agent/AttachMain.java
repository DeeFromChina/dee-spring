package org.dee.agent;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

public class AttachMain {

    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        VirtualMachine vm = VirtualMachine.attach("11675");
        vm.loadAgent("/Users/frieda.li/Desktop/code/sunline/SunERP/Application_Server/sunerp-wecom/sunerp-wecom-agent/target/sunerp-wecom-agent-0.0.1-SNAPSHOT.jar");
    }

    public static String agentJarPath() {
        String path = System.getProperty("java.class.path");
        String[] paths = path.split(":");
        for (String s : paths) {
            if(s.endsWith("sunerp-wecom-agent-0.0.1-SNAPSHOT.jar")){
                return s;
            }
        }
        throw new RuntimeException();
    }

}
