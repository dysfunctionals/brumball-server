import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class TeamManager{

    private final List<List<User>> teams;
    private final Map<String, User> users;
    private static final int TEAM_NUMBER = 6;

    public TeamManager(){
        teams = new CopyOnWriteArrayList<List<User>>();
        for(int i = 0; i < TEAM_NUMBER; i++){
            teams.add(new CopyOnWriteArrayList<User>());
        }
        users = new ConcurrentHashMap<String, User>();
    }

    public User registerUser(){
        Random reallyIntelligentlyNamedVariable = new Random();
        int lowestTeamCount = Integer.MAX_VALUE;
        int lowestTeam = -1;
        for(int i = 0; i < TEAM_NUMBER; i++){
            if(teams.get(i).size() < lowestTeamCount){
                lowestTeam = i;
                lowestTeamCount = teams.get(i).size();
            }
        }
        String token = Integer.toHexString(reallyIntelligentlyNamedVariable.nextInt());
        User newUser = new User(token, lowestTeam);
        teams.get(lowestTeam).add(newUser);
        users.put(token, newUser);
        return newUser;
    }

    private void removeInactive(){
        ArrayList<User> users = new ArrayList<User>();
        ArrayList<String> deadTokens = new ArrayList<String>();
        for(String token : this.users.keySet()){
            if(!this.users.get(token).isAlive()){
                users.add(this.users.get(token));
                deadTokens.add(token);
            }
        }

        for(User dead : users){
            for(List<User> team : teams){
                team.remove(dead);
            }
        }

        for(String dead : deadTokens){
            Logger.log("Killed: " + dead, Logger.Level.INFO);
            this.users.remove(dead);
        }
    }

    public User getUser(String token){
        User user = users.get(token);
        if(user == null){
            return null;
        } else if (!user.isAlive()){
            users.remove(token);
            for(List<User> team : teams){
                team.remove(user);
            }
            return null;
        } else {
            return user;
        }
    }

    public double teamValue(int teamNumber){
        removeInactive();
        List<User> team = teams.get(teamNumber);
        double value = 0.0;
        for(User yes : team){
            value += yes.getVote();
        }
        if(team.size()!=0) value /= (double) team.size();
        Logger.log(value, Logger.Level.DEBUG);
        return value;
    }
    
    public List<Double> teamList(){
        ArrayList<Double> teams = new ArrayList<Double>();
        for(int i = 0; i < TEAM_NUMBER; i++) teams.add(teamValue(i));
        return teams;
    }

    public int getUserCount(){
        return users.size();
    }
}
