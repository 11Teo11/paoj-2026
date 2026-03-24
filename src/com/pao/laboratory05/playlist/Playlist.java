package com.pao.laboratory05.playlist;

import java.util.Arrays;

public class Playlist {
    private String name;
    private Song[] songs;

    Playlist(String name){
        this.name = name;
        this.songs = new Song[0];
    }

    public String getName(){
        return this.name;
    }

    public void addSong(Song song){
        Song[] tmp = new Song[songs.length + 1];
        System.arraycopy(songs, 0, tmp, 0, songs.length);
        tmp[tmp.length - 1] = song;
        songs = tmp;
    }

    public void printSortedByTitle(){
        Song[] copy = songs.clone();
        Arrays.sort(copy);
        for(Song s : copy)
            System.out.println(s);
    }


    public void printSortedByDuration(){
        Song[] copy = songs.clone();
        Arrays.sort(copy, new SongDurationComparator());
        for(Song s : copy)
            System.out.println(s);
    }

    public int getTotalDuration(){
        int suma = 0;
        for(Song s : songs){
            suma += s.durationSeconds();
        }
        return suma;
    }
}
