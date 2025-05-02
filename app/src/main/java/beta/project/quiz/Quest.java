package beta.project.quiz;

public class Quest {
    Integer id;
    String question, ans1, ans2, ans3, url;

    public Quest(int id,String qu, String a1, String a2, String a3,String img) {
        this.id=id;
        this.question = qu;
        this.ans1=a1;
        this.ans2=a2;
        this.ans3=a3;
        this.url = img;
    }
}