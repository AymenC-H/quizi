package beta.project.quiz;

public class Quizz {
    int id;
    String nameq;
    int nb_passes;
    int mxtime;
    int[] ids_questions;
    public Quizz(int id,String name,int nbp, int[] idqs,int time) {
        this.id = id;
        this.nameq = name;
        ids_questions=idqs;
        nb_passes = nbp;
        mxtime=time;
    }
}