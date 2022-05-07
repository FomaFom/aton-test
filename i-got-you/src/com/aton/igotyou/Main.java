package com.aton.igotyou;

public class Main {

    public static void main(String[] args) {
        String[][] lyrics = {
                {"Cher", "They say we're young and we don't know \nWe won't find out until we grow"},
                {"Sonny", "Well I don't know if all that's true \n'Cause you got me, and baby I got you"},
                {"Sonny", "Babe"},
                {"Sonny, Cher", "I got you babe \nI got you babe"},
                {"Cher", "They say our love won't pay the rent \nBefore it's earned, our money's all been spent"},
                {"Sonny", "I guess that's so, we don't have a pot \nBut at least I'm sure of all the things we got"},
                {"Sonny", "Babe"},
                {"Sonny, Cher", "I got you babe \nI got you babe"},
                {"Sonny", "I got flowers in the spring \nI got you to wear my ring"},
                {"Cher", "And when I'm sad, you're a clown \nAnd if I get scared, you're always around"},
                {"Cher", "So let them say your hair's too long \n'Cause I don't care, with you I can't go wrong"},
                {"Sonny", "Then put your little hand in mine \nThere ain't no hill or mountain we can't climb"},
                {"Sonny", "Babe"},
                {"Sonny, Cher", "I got you babe \nI got you babe"},
                {"Sonny", "I got you to hold my hand"},
                {"Cher", "I got you to understand"},
                {"Sonny", "I got you to walk with me"},
                {"Cher", "I got you to talk with me"},
                {"Sonny", "I got you to kiss goodnight"},
                {"Cher", "I got you to hold me tight"},
                {"Sonny", "I got you, I won't let go"},
                {"Cher", "I got you to love me so"},
                {"Sonny, Cher", "I got you babe \nI got you babe \nI got you babe \nI got you babe \nI got you babe"}
        };

        Semaphore semaphore = new Semaphore();

        CherSinger sonnySinger = new CherSinger(lyrics, semaphore);
        SonnySinger cherSinger = new SonnySinger(lyrics, semaphore);

        Thread sonny = new Thread(sonnySinger);
        Thread cher = new Thread(cherSinger);


        cher.start();
        sonny.start();

    }

    static class CherSinger implements Runnable {

        String[][] lyrics;
        Semaphore semaphore;

        public CherSinger(String[][] lyrics, Semaphore semaphore) {
            this.lyrics = lyrics;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            synchronized (semaphore) {
                for (int i = 0; i < lyrics.length; i++) {
                    if (semaphore.isSonnySang) {
                        try {
                            semaphore.notify();
                            if (lyrics[i][0].equals("Sonny, Cher") || lyrics[i][0].equals("Cher")) {
                                System.out.println(lyrics[i][0] + ": " + lyrics[i][1]+"\r\n");
                            }
                            semaphore.isSonnySang = false;
                            if (i - 1 != lyrics.length) {
                                semaphore.wait();
                            }
                        } catch (InterruptedException ex) {
                            System.out.println("My throat is hurt!..");
                        }
                    }
                }
                System.out.println("Cher: Thank you for listening!!!");
            }
        }

    }

    static class SonnySinger implements Runnable {
        String[][] lyrics;
        Semaphore semaphore;

        public SonnySinger(String[][] lyrics, Semaphore semaphore) {
            this.lyrics = lyrics;
            this.semaphore = semaphore;
        }

        @Override
        public void run() {
            synchronized (semaphore) {
                for (int i = 0; i < lyrics.length; i++) {
                    if (!semaphore.isSonnySang) {
                        try {
                            semaphore.notify();
                            if (lyrics[i][0].equals("Sonny, Cher") || lyrics[i][0].equals("Sonny")) {
                                System.out.println(lyrics[i][0] + ": " + lyrics[i][1]+"\r\n");
                            }

                            semaphore.isSonnySang = true;
                            semaphore.wait();
                        } catch (InterruptedException ex) {
                            System.out.println("My throat is hurt!..");
                        }
                    }
                }
                System.out.println("Sonny: Thank you for listening!!!");
                semaphore.notify();
            }
        }
    }

    static class Semaphore {
        public boolean isSonnySang = false;
    }


}
