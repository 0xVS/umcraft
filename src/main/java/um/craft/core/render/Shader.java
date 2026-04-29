package um.craft.core.render;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    private final int programId;

    private int vertexShaderId;
    private int fragmentShaderId;

    public Shader() throws Exception {
        programId = glCreateProgram();
        if (programId == 0){
            throw new Exception("Du huso der shader is kaputt");
        }
    }

    public void setVec2(String name, float x, float y){
        int location = glGetUniformLocation(programId, name);
        if (location != -1){
            glUniform2f(location, x, y);
        } else {
            System.err.println("Uniform " + name + " nicht gefunden");
        }
    }

    public void setMat4(String name, Matrix4f matrix){
        int location = glGetUniformLocation(programId, name);
        try (MemoryStack stack = MemoryStack.stackPush()) {
             var buffer = stack.mallocFloat(16);
             matrix.get(buffer);
             glUniformMatrix4fv(location, false, buffer);
        }
    }

    public void setFloat(String name, float value){
        int location = glGetUniformLocation(programId, name);
        if (location != -1){
            glUniform1f(location, value);
        }
    }


    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    public int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) throw new Exception("was" + shaderType);

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            String log = glGetShaderInfoLog(shaderId, 1024);
            System.out.println(log);
            throw new Exception("Shader Exception");
        }

        glAttachShader(programId, shaderId);
        return shaderId;
    }

    public void link() throws Exception {
        glLinkProgram(programId);
        String log = glGetShaderInfoLog(programId, 1024);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) throw new Exception("Linking Exception" + log);

        if (vertexShaderId != 0){
            glDetachShader(programId, vertexShaderId);
            glDeleteShader(vertexShaderId);
        }

        if (fragmentShaderId != 0){
            glDetachShader(programId, fragmentShaderId);
            glDeleteShader(fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0)  System.err.println("WARNUNG: Validiernug vom Shader code: " + log);
    }
    public void bind() {
        glUseProgram(programId);
    }
    public void unbind(){
        glUseProgram(0);
    }
    public void cleanup(){
        unbind();
        if (programId != 0) glDeleteProgram(programId);
        System.out.println(">>>>>>>> Shader cleanup fertig");
    }
}
