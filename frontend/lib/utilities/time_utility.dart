
import 'package:intl/intl.dart';

class TimeUtility {
  static String timeToString(DateTime dateTime) {
    final difference = DateTime.now().difference(dateTime);

    if (difference.inMinutes < 1) {
      return "방금";
    }

    else if (difference.inHours < 1) {
      return '${difference.inMinutes}분전';
    }

    else if (difference.inDays < 1) {
      return '${difference.inHours}시간전';
    }

    else if (difference.inDays < 2) {
      return '어제';
    }

    if (dateTime.year == DateTime.now().year) {
      return DateFormat('MM.dd').format(dateTime);
    }

    return DateFormat('yyyy.MM.dd').format(dateTime);
  }
}