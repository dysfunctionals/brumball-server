public abstract class Logger {

    public enum Level {
        INFO() {
            @Override
            public void log(String message){
                System.out.println("[INFO] : " + message);
            }
        },
        DEBUG() {
            @Override
            public void log(String message){
                System.out.println("[DEBUG] : " + message);
            }
        },
        WARN(){
            @Override
            public void log(String message){
                System.out.println("[WARN] : " + message);
            }
        },
        SERIOUS() {
            @Override
            public void log(String message){
                System.out.println("[SERIOUS] : " + message);
            }
        },
        FATAL() {
            @Override
            public void log(String message) {
                System.out.println("[FATAL] : " + message);
                System.exit(1);
            }
        };

        public void log(String message) {}
    }

    public static void log(String message, Level level){
        level.log(message);
    }

}
