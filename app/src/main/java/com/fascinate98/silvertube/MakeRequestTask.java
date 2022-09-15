//package com.fascinate98.silvertube;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.AsyncTask;
//import android.util.Log;
//
//import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
//import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
//import com.google.api.client.http.HttpRequestInitializer;
//import com.google.api.client.googleapis.json.GoogleJsonResponseException;
//import com.google.api.client.http.HttpTransport;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.jackson2.JacksonFactory;
//import com.google.api.services.youtube.YouTube;
//import com.google.api.services.youtube.model.Channel;
//import com.google.api.services.youtube.model.ChannelListResponse;
//import com.google.api.services.youtube.model.PlaylistItem;
//import com.google.api.services.youtube.model.PlaylistItemListResponse;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//
//public class MakeRequestTask extends AsyncTask<Void, Void, YoutubeUser> {
//
//    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
//    private static final JsonFactory JSON_FACTORY = new JacksonFactory();
//    private YouTube mService = null;
//    private GoogleAccountCredential credential;
//    private YoutubeUser youtubeUser;
//    private RequestInfo ri;
//    private Activity activity;
//
//    MakeRequestTask(GoogleAccountCredential credential, ProgressDialog pd, Activity activity) {
//        this.credential =credential;
//        mService = new YouTube.Builder(
//                HTTP_TRANSPORT, JSON_FACTORY, credential)
//                .setApplicationName("DataApi")
//
//                .build();
//        this.ri = RequestInfo.getInstance();
//        this.youtubeUser = new YoutubeUser();
//        this.youtubeUser.setAccountMail(this.credential.getSelectedAccountName());
//        this.activity = activity;
//    }
//
//    @Override
//    protected YoutubeUser doInBackground(Void... params) {
//        getDataFromApi();
//        return youtubeUser;
//    }
//
//    private void getDataFromApi() {
//        ChannelListResponse channelResult = null;
//        try {
//            channelResult = mService.channels().list("snippet,contentDetails,statistics")
//                    .setMine(true)
//                    .setFields("items/contentDetails,nextPageToken,pageInfo")
//                    .execute();
//
//            System.out.println(channelResult);
//
//
//            List<Channel> channelsList = channelResult.getItems();
//
//            if (channelsList != null) {
//
//
//                // The user's default channel is the first item in the list.
//                // Extract the playlist ID for the channel's videos from the
//                // API response.
//                String uploadPlaylistId =
//                        channelsList.get(0).getContentDetails().getRelatedPlaylists().getUploads();
//
//                // Define a list to store items in the list of uploaded videos.
//                List<PlaylistItem> playlistItemList = new ArrayList<PlaylistItem>();
//
//                // Retrieve the playlist of the channel's uploaded videos.
//                YouTube.PlaylistItems.List playlistItemRequest =
//                        mService.playlistItems().list("id,contentDetails,snippet");
//                playlistItemRequest.setPlaylistId(uploadPlaylistId);
//
//                // Only retrieve data used in this application, thereby making
//                // the application more efficient. See:
//                // https://developers.google.com/youtube/v3/getting-started#partial
//                playlistItemRequest.setFields("items(contentDetails/videoId,snippet/title,snippet/publishedAt),nextPageToken,pageInfo");
//
//                String nextToken = "";
//
//                // Call the API one or more times to retrieve all items in the
//                // list. As long as the API response returns a nextPageToken,
//                // there are still more items to retrieve.
//                do {
//                    playlistItemRequest.setPageToken(nextToken);
//                    PlaylistItemListResponse playlistItemResult = playlistItemRequest.execute();
//
//                    playlistItemList.addAll(playlistItemResult.getItems());
//
//                    nextToken = playlistItemResult.getNextPageToken();
//                } while (nextToken != null);
//
//                // Prints information about the results.
//                processInfo(playlistItemList.iterator());
//            }
//
//        } catch (UserRecoverableAuthIOException e) {
//            Log.d("1111111111111","111111111111");
////            System.out.println("UserRecoverableAuthIOException");
////            this.activity.startActivityForResult(
////                    ((UserRecoverableAuthIOException) e).getIntent(),
////                    MainActivity.REQUEST_AUTHORIZATION);
//        }
//        catch (GoogleJsonResponseException e) {
//            e.printStackTrace();
//            Log.d("22222222222222222","2222222222222");
//            System.err.println("There was a service error: " + e.getDetails().getCode() + " : " + e.getDetails().getMessage());
//
//        } catch (Throwable t) {
//            t.printStackTrace();
//        }
//    }
//
//    private void processInfo(Iterator<PlaylistItem> playlistEntries) {
//
//        List<YoutubeVideo> myVideo = new ArrayList<>();
//
//        while (playlistEntries.hasNext()) {
//            PlaylistItem playlistItem = playlistEntries.next();
//            YoutubeVideo vid = new YoutubeVideo(playlistItem.getSnippet().getTitle(),
//                    playlistItem.getContentDetails().getVideoId(),
//                    playlistItem.getSnippet().getPublishedAt().toString());
//            myVideo.add(vid);
//        }
//        youtubeUser.addVideoContent(myVideo);
//    }
//
//
//
//}