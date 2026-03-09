package engine;

import engine.renderer.Renderer;

public class Engine {

    private Window window;
    private Renderer renderer;

    public void run() {
        init();

        while (!window.shouldClose()) {
            update();
            render();
        }

        cleanup();
    }

    private void init() {
        window = new Window(1920, 1080, "Sim Engine");
        renderer = new Renderer(window);
    }

    private void update() {
        window.pollEvents();
    }

    private void render() {
        renderer.render();
    }

    private void cleanup() {
        renderer.cleanup();
        window.cleanup();
    }
}