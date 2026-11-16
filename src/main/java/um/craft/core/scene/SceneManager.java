package um.craft.core.scene;

import um.craft.core.render.font.NanoVGFontRenderer;

public class SceneManager {
    private final SceneContext context;
    private Scene currentScene;
    private Scene nextScene;



    public SceneManager(SceneContext context){
        this.context = context;
    }

    public void setScene(Scene scene){
        this.nextScene = scene;
    }

    public Scene getScene(){
        return currentScene;
    }

    public void transition() {
        if (nextScene == null) return;
        if (currentScene != null) currentScene.onExit();

        currentScene = nextScene;
        nextScene = null;

        currentScene.onEnter(context);
    }
    // public void update(float deltaTime){ if (currentScene != null) currentScene.update(deltaTime); }
    public void update(float deltaTime){
        transition();
        if (currentScene != null) currentScene.update(deltaTime);
    }

    public void render(){
        if (currentScene == null) return;

        currentScene.render();
        NanoVGFontRenderer font = context.getFont();

        font.beginFrame(context.getWindow().getWidth(), context.getWindow().getHeight(), 1f);
        try {
            currentScene.renderText(font);
        } finally {
            font.endFrame();
        }

    }

    public void cleanup(){
        if (currentScene != null){
            currentScene.onExit();
            currentScene = null;
        }
        nextScene = null;
    }

    public void resize(int width, int height){
        if (currentScene != null) currentScene.resize(width, height);
    }
}
