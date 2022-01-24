package com.kay;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

public class Attach {


    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException,
            AgentInitializationException {

        // TargetClass pid
        final VirtualMachine attach = VirtualMachine.attach("35592");
        // Agent path
        attach.loadAgent("C:\\kaybee\\code\\bytecode-lab\\javassist-agent-01\\build\\libs\\javassist-agent-01-1.0-SNAPSHOT.jar");
    }
}
