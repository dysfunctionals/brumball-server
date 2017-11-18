public class User{
    private final String token;
    private volatile int vote;
    private final int team;
    private volatile long lastModified;

    public User(String token, int team){
        this.token = token;
        this.vote = 0;
        this.team = team;
        modify();
    }

    public String getToken(){
        return token;
    }

    public int getVote(){
        return vote;
    }

    public void setVote(int vote){
        vote = (vote > 1 ? 1 : (vote < -1 ? -1 : vote));
        modify();
        this.vote = vote;
    }

    public int getTeam(){
        return team;
    }

    public void modify(){
        lastModified = System.currentTimeMillis();
    }

    public boolean isAlive(){
        if( System.currentTimeMillis() - lastModified > 15000 ){
            return false;
        } else {
            return true;
        }
    }
}
