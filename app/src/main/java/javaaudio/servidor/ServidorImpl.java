package javaaudio.servidor;

import java.io.IOException;
import java.io.InputStream;

import com.google.protobuf.ByteString;
import com.proto.audio.Audio.DataChunkResponse;
import com.proto.audio.Audio.DownloadFileRequest;
import com.proto.audio.AudioServiceGrpc.AudioServiceImplBase;

import io.grpc.stub.StreamObserver;

public class ServidorImpl extends AudioServiceImplBase{

    @Override
    public void downloadAudio(DownloadFileRequest request, StreamObserver<DataChunkResponse> responseObserver) {
        String archivoNombre = "/" + request.getNombre();
        System.out.println(archivoNombre);

        InputStream fileStream = ServidorImpl.class.getResourceAsStream(archivoNombre);

        int bufferSize = 1024;
        byte [] buffer = new byte[bufferSize];
        int lenght;

        try{
            while ((lenght = fileStream.read(buffer, 0 , bufferSize))!= -1) {
                DataChunkResponse respuesta = DataChunkResponse.newBuilder()
                    .setData(ByteString.copyFrom(buffer, 0 ,lenght))
                    .build();

                System.out.println(".");
                responseObserver.onNext(respuesta);
            }
            fileStream.close();
        } catch (IOException e){
            System.out.println("No se pudo obtener el archivo " + archivoNombre);
        }

        responseObserver.onCompleted();
    }
    
}
