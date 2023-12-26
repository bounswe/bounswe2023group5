using System.Collections;
using System.IO;
using Unity.VisualScripting;
using UnityEngine;

public static class FileController
{
	// void Start()
	// {
	// 	pdfFileType = NativeFilePicker.ConvertExtensionToFileType( "pdf" ); // Returns "application/pdf" on Android and "com.adobe.pdf" on iOS
	// 	Debug.Log( "pdf's MIME/UTI is: " + pdfFileType );
	// }

	public static string PickAnImageFile()			
	{
		// Don't attempt to import/export files if the file picker is already open
		if( NativeFilePicker.IsFilePickerBusy() )
			return "";
		string _path = "";
		// Pick a PDF file
		string permission = NativeFilePicker.PickFile( ( path ) =>
		{
			if( path == null )
				Debug.Log( "Operation cancelled" );
			else
				Debug.Log( "Picked file: " + path );
			_path = path;
		}, new string[] { "image/*" } );
		Debug.Log( "Permission result: " + permission );
		return _path;
	}
	
	public static void PickAFile(string[] fileType)			
	{
		// Don't attempt to import/export files if the file picker is already open
		if( NativeFilePicker.IsFilePickerBusy() )
			return;
				
		// Pick a PDF file
		string permission = NativeFilePicker.PickFile( ( path ) =>
		{
			if( path == null )
				Debug.Log( "Operation cancelled" );
			else
				Debug.Log( "Picked file: " + path );
		}, fileType );

		Debug.Log( "Permission result: " + permission );
	}
	
	public static void PickMultipleImageFiles()			
	{
		// Don't attempt to import/export files if the file picker is already open
		if( NativeFilePicker.IsFilePickerBusy() )
			return;
		
#if UNITY_ANDROID
		// Use MIMEs on Android
		string[] fileTypes = new string[] { "image/*", "video/*" };
#else
		// Use UTIs on iOS
		string[] fileTypes = new string[] { "public.image", "public.movie" };
#endif

		// Pick image(s) and/or video(s)
		NativeFilePicker.Permission permission = NativeFilePicker.PickMultipleFiles( ( paths ) =>
		{
			if( paths == null )
				Debug.Log( "Operation cancelled" );
			else
			{
				for( int i = 0; i < paths.Length; i++ )
					Debug.Log( "Picked file: " + paths[i] );
			}
		}, fileTypes );

		Debug.Log( "Permission result: " + permission );
	}
	
	public static void PickMultipleFiles(string[] fileTypes)			
	{
		// Don't attempt to import/export files if the file picker is already open
		if( NativeFilePicker.IsFilePickerBusy() )
			return;

		// Pick image(s) and/or video(s)
		NativeFilePicker.Permission permission = NativeFilePicker.PickMultipleFiles( ( paths ) =>
		{
			if( paths == null )
				Debug.Log( "Operation cancelled" );
			else
			{
				for( int i = 0; i < paths.Length; i++ )
					Debug.Log( "Picked file: " + paths[i] );
			}
		}, fileTypes );

		Debug.Log( "Permission result: " + permission );
	}
	
	public static void SaveATxtFile()			
	{
		// Don't attempt to import/export files if the file picker is already open
		if( NativeFilePicker.IsFilePickerBusy() )
			return;
		
		// Create a dummy text file
		string filePath = Path.Combine( Application.temporaryCachePath, "test.txt" );
		File.WriteAllText( filePath, "Hello world!" );

		// Export the file
		NativeFilePicker.Permission permission = NativeFilePicker.ExportFile( filePath, ( success ) => Debug.Log( "File exported: " + success ) );

		Debug.Log( "Permission result: " + permission );
	}

	// Example code doesn't use this function but it is here for reference. It's recommended to ask for permissions manually using the
	// RequestPermissionAsync methods prior to calling NativeFilePicker functions
	public static async  void RequestPermissionAsynchronously( bool readPermissionOnly = false )
	{
		NativeFilePicker.Permission permission = await NativeFilePicker.RequestPermissionAsync( readPermissionOnly );
		Debug.Log( "Permission result: " + permission );
	}

	
}
